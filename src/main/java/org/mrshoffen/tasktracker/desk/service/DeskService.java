package org.mrshoffen.tasktracker.desk.service;

import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.desk.exception.DeskAlreadyExistsException;
import org.mrshoffen.tasktracker.desk.model.dto.DeskCreateDto;
import org.mrshoffen.tasktracker.desk.model.dto.DeskResponseDto;
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

    public Flux<DeskResponseDto> getAllDesksInWorkspace(String workspace, UUID userId) {
        return getWorkspaceId(workspace, userId)
                .flatMapMany(workspaceId ->
                        deskRepository
                                .findAllByUserIdAndWorkspaceId(userId, workspaceId)
                )
                .map(desk ->
                        workspaceMapper
                                .toDeskResponse(desk, workspace)
                );
    }

    public Mono<UUID> getWorkspaceId(String workspaceName, UUID userId) {
        return workspaceClient
                .getBoardId(workspaceName, userId);

    }

    public Mono<DeskResponseDto> createDesk(DeskCreateDto dto, UUID userId, String workspaceName) {
        return getWorkspaceId(workspaceName, userId)
                .flatMap(workspaceId -> {
                    Desk desk = workspaceMapper.toDesk(dto, userId, workspaceId);
                    return deskRepository.save(desk);
                })
                .onErrorMap(DataIntegrityViolationException.class, e ->
                        new DeskAlreadyExistsException(
                                "Задача с именем '%s' уже существует на доске '%s'"
                                        .formatted(dto.name(), workspaceName)
                        )
                )
                .map(savedDesk -> workspaceMapper.toDeskResponse(savedDesk, workspaceName));
    }
}
