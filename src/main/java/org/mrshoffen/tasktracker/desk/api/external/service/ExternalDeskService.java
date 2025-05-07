package org.mrshoffen.tasktracker.desk.api.external.service;

import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.commons.web.dto.DeskResponseDto;
import org.mrshoffen.tasktracker.commons.web.exception.EntityAlreadyExistsException;
import org.mrshoffen.tasktracker.desk.client.WorkspaceClient;
import org.mrshoffen.tasktracker.desk.mapper.WorkspaceMapper;
import org.mrshoffen.tasktracker.desk.model.dto.DeskCreateDto;
import org.mrshoffen.tasktracker.desk.model.entity.Desk;
import org.mrshoffen.tasktracker.desk.repository.DeskRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExternalDeskService {

    private final WorkspaceMapper workspaceMapper;

    private final DeskRepository deskRepository;

    private final WorkspaceClient workspaceClient;

    public Mono<DeskResponseDto> createDeskInUserWorkspace(DeskCreateDto dto, UUID userId, UUID workspaceId) {
        return workspaceClient
                .ensureUserOwnsWorkspace(userId, workspaceId)
                .then(
                        Mono.defer(() -> {
                            Desk desk = workspaceMapper.toDesk(dto, userId, workspaceId);
                            return deskRepository.save(desk);
                        })
                )
                .onErrorMap(DataIntegrityViolationException.class, e ->
                        new EntityAlreadyExistsException(
                                "Доска с именем '%s' уже существует в пространстве '%s'"
                                        .formatted(dto.name(), workspaceId)
                        )
                )
                .map(workspaceMapper::toDeskResponse);
    }

    public Flux<DeskResponseDto> getAllDesksInUserWorkspace(UUID workspaceId, UUID userId) {
        return workspaceClient
                .ensureUserOwnsWorkspace(userId, workspaceId)
                .thenMany(
                        deskRepository
                                .findAllByUserIdAndWorkspaceId(userId, workspaceId)
                )
                .map(workspaceMapper::toDeskResponse);
    }
}
