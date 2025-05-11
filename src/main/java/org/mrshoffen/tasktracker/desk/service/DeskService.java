package org.mrshoffen.tasktracker.desk.service;

import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.commons.web.dto.DeskResponseDto;
import org.mrshoffen.tasktracker.commons.web.exception.EntityAlreadyExistsException;
import org.mrshoffen.tasktracker.commons.web.exception.EntityNotFoundException;
import org.mrshoffen.tasktracker.desk.event.DeskEventPublisher;
import org.mrshoffen.tasktracker.desk.mapper.WorkspaceMapper;
import org.mrshoffen.tasktracker.desk.model.dto.DeskCreateDto;
import org.mrshoffen.tasktracker.desk.model.dto.OrderIndexUpdateDto;
import org.mrshoffen.tasktracker.desk.model.entity.Desk;
import org.mrshoffen.tasktracker.desk.repository.DeskRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mrshoffen.tasktracker.commons.utils.OrderIndexGenerator.next;

@Service
@RequiredArgsConstructor
public class DeskService {

    private final WorkspaceMapper workspaceMapper;

    private final DeskRepository deskRepository;

    private final DeskEventPublisher eventPublisher;

    //safe
    public Mono<DeskResponseDto> createDeskInUserWorkspace(DeskCreateDto dto, UUID userId, UUID workspaceId) {
        return deskRepository
                .findMaxOrderIndexInWorkspace(workspaceId)
                .flatMap(currentMaxOrderIndex -> {
                    Desk desk = workspaceMapper.toDesk(dto, userId, workspaceId);
                    desk.setOrderIndex(next(currentMaxOrderIndex));
                    return deskRepository.save(desk);
                })
                .onErrorMap(DataIntegrityViolationException.class, e ->
                        new EntityAlreadyExistsException(
                                "Доска с именем '%s' уже существует в пространстве '%s'"
                                        .formatted(dto.name(), workspaceId)
                        )
                )
                .map(workspaceMapper::toDeskResponse);
    }

    //safe
    public Flux<DeskResponseDto> getAllDesksInUserWorkspace(UUID workspaceId) {
        return deskRepository
                .findAllByWorkspaceId(workspaceId)
                .map(workspaceMapper::toDeskResponse);
    }

    //safe
    public Mono<Void> deleteUserDesk(UUID workspaceId, UUID deskId) {
        return deskRepository
                .findByIdAndWorkspaceId(deskId, workspaceId)
                .doOnSuccess(desk -> {
                    if (desk != null) {
                        eventPublisher.publishDeskDeletedEvent(desk);
                    }
                })
                .switchIfEmpty(
                        Mono.error(new EntityNotFoundException(
                                "Доска с id %s не найдена в данном пространстве"
                                        .formatted(deskId.toString())
                        ))
                )
                .flatMap(deskRepository::delete);
    }

    public Mono<DeskResponseDto> updateDeskOrder(UUID workspaceId, UUID deskId, OrderIndexUpdateDto orderIndexUpdateDto) {
        return deskRepository
                .findByIdAndWorkspaceId(deskId, workspaceId)
                .switchIfEmpty(
                        Mono.error(new EntityNotFoundException(
                                "Доска с id %s не найдена в данном пространстве"
                                        .formatted(deskId.toString())
                        ))
                )
                .flatMap(desk -> {
                    desk.setOrderIndex(orderIndexUpdateDto.updatedIndex());
                    return deskRepository.save(desk);
                })
                .map(workspaceMapper::toDeskResponse);
    }

    public Mono<Void> deleteAllDesksInWorkspace(UUID workspaceId) {
        return deskRepository
                .deleteAllByWorkspaceId(workspaceId);
    }

    public Mono<DeskResponseDto> getDeskByIdInWorkspace(UUID workspaceId, UUID deskId) {
        return deskRepository
                .findByIdAndWorkspaceId(deskId, workspaceId)
                .map(workspaceMapper::toDeskResponse);
    }
}
