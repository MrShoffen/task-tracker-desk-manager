package org.mrshoffen.tasktracker.desk.service;

import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.commons.web.dto.DeskResponseDto;
import org.mrshoffen.tasktracker.desk.exception.DeskAlreadyExistsException;
import org.mrshoffen.tasktracker.desk.exception.DeskNotFoundException;
import org.mrshoffen.tasktracker.desk.model.dto.DeskCreateDto;
import org.mrshoffen.tasktracker.desk.model.entity.Desk;
import org.mrshoffen.tasktracker.desk.repository.DeskRepository;
import org.mrshoffen.tasktracker.desk.util.client.WorkspaceClient;
import org.mrshoffen.tasktracker.desk.util.mapper.WorkspaceMapper;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeskService {

    private final WorkspaceMapper workspaceMapper;

    private final DeskRepository deskRepository;

    private final WorkspaceClient workspaceClient;

    public Mono<DeskResponseDto> createDesk(DeskCreateDto dto, UUID userId, UUID workspaceId) {
        return workspaceClient
                .validateWorkspaceStructure(userId, workspaceId)
                .then(
                        Mono.defer(() -> {
                            Desk desk = workspaceMapper.toDesk(dto, userId, workspaceId);
                            return deskRepository.save(desk);
                        })
                )
                .onErrorMap(DataIntegrityViolationException.class, e ->
                        new DeskAlreadyExistsException(
                                "Доска с именем '%s' уже существует в пространстве '%s'"
                                        .formatted(dto.name(), workspaceId)
                        )
                )
                .map(workspaceMapper::toDeskResponse);
    }

    public Flux<DeskResponseDto> getAllDesksInWorkspace(UUID workspaceId, UUID userId) {
        return workspaceClient
                .validateWorkspaceStructure(userId, workspaceId)
                .thenMany(
                        deskRepository
                                .findAllByUserIdAndWorkspaceId(userId, workspaceId)
                )
                .map(workspaceMapper::toDeskResponse
                );
    }

    public Mono<DeskResponseDto> getUserDeskInWorkspace(UUID userId, UUID workspaceId, UUID deskId) {
        return workspaceClient
                .validateWorkspaceStructure(userId, workspaceId)
                .then(
                        Mono.defer(() ->
                                deskRepository.findByUserIdAndWorkspaceIdAndId(userId, workspaceId, deskId)
                        )
                )
                .map(workspaceMapper::toDeskResponse)
                .switchIfEmpty(
                        Mono.error(new DeskNotFoundException(
                                "Доска с id %s не найдена в данном пространстве у пользователя"
                                        .formatted(deskId.toString())
                        ))
                );
    }
}
