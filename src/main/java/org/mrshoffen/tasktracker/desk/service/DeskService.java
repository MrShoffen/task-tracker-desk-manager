package org.mrshoffen.tasktracker.desk.service;

import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.commons.web.dto.DeskResponseDto;
import org.mrshoffen.tasktracker.commons.web.exception.EntityAlreadyExistsException;
import org.mrshoffen.tasktracker.commons.web.exception.EntityNotFoundException;
import org.mrshoffen.tasktracker.desk.event.DeskEventPublisher;
import org.mrshoffen.tasktracker.desk.mapper.DeskMapper;
import org.mrshoffen.tasktracker.desk.model.dto.create.DeskCreateDto;
import org.mrshoffen.tasktracker.desk.model.dto.edit.DeskUpdateColorDto;
import org.mrshoffen.tasktracker.desk.model.dto.edit.DeskUpdateNameDto;
import org.mrshoffen.tasktracker.desk.model.dto.edit.OrderIndexUpdateDto;
import org.mrshoffen.tasktracker.desk.model.entity.Desk;
import org.mrshoffen.tasktracker.desk.repository.DeskRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mrshoffen.tasktracker.commons.utils.OrderIndexGenerator.next;

@Service
@RequiredArgsConstructor
public class DeskService {

    private final DeskMapper deskMapper;

    private final DeskRepository deskRepository;

    private final DeskEventPublisher eventPublisher;
    private final DeskEventPublisher deskEventPublisher;

    //safe
    public Mono<DeskResponseDto> createDeskInUserWorkspace(DeskCreateDto dto, UUID userId, UUID workspaceId) {
        return deskRepository
                .findMaxOrderIndexInWorkspace(workspaceId)
                .flatMap(currentMaxOrderIndex -> {
                    Desk desk = deskMapper.toDesk(dto, userId, workspaceId);
                    desk.setOrderIndex(next(currentMaxOrderIndex));
                    return deskRepository.save(desk);
                })
                .onErrorMap(DataIntegrityViolationException.class, e ->
                        new EntityAlreadyExistsException(
                                "Доска с именем '%s' уже существует в пространстве '%s'"
                                        .formatted(dto.name(), workspaceId)
                        )
                )
                .map(deskMapper::toDeskResponse)
                .doOnSuccess(eventPublisher::publishDeskCreatedEvent);
    }

    //safe
    public Flux<DeskResponseDto> getAllDesksInUserWorkspace(UUID workspaceId) {
        return deskRepository
                .findAllByWorkspaceId(workspaceId)
                .map(deskMapper::toDeskResponse);
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

    public Mono<Void> deleteAllDesksInWorkspace(UUID workspaceId) {
        return deskRepository
                .deleteAllByWorkspaceId(workspaceId);
    }


    public Mono<DeskResponseDto> updateDeskOrder(UUID workspaceId, UUID deskId, OrderIndexUpdateDto dto, UUID userId) {
        return getDeskOrThrow(workspaceId, deskId)
                .flatMap(desk -> {
                    desk.setOrderIndex(dto.updatedIndex());
                    return deskRepository.save(desk);
                })
                .map(deskMapper::toDeskResponse)
                .doOnSuccess(desk -> deskEventPublisher
                        .publishDeskUpdatedEvent(workspaceId, deskId, "orderIndex", dto.updatedIndex(), userId));

    }

    public Mono<DeskResponseDto> updateDeskName(UUID workspaceId, UUID deskId, DeskUpdateNameDto dto, UUID userId) {
        return getDeskOrThrow(workspaceId, deskId)
                .flatMap(desk -> {
                    desk.setName(dto.newName());
                    return deskRepository.save(desk);
                })

                .onErrorMap(DuplicateKeyException.class, e ->
                        new EntityAlreadyExistsException(
                                "Доска с именем '%s' уже существует"
                                        .formatted(dto.newName())
                        )
                )
                .map(deskMapper::toDeskResponse)
                .doOnSuccess(desk -> deskEventPublisher
                        .publishDeskUpdatedEvent(workspaceId, deskId, "name", dto.newName(), userId));

    }

    public Mono<DeskResponseDto> updateDeskColor(UUID workspaceId, UUID deskId, DeskUpdateColorDto dto, UUID userId) {
        return getDeskOrThrow(workspaceId, deskId)
                .flatMap(desk -> {
                    desk.setColor(dto.newColor());
                    return deskRepository.save(desk);
                })
                .map(deskMapper::toDeskResponse)
                .doOnSuccess(desk -> deskEventPublisher
                        .publishDeskUpdatedEvent(workspaceId, deskId, "color", dto.newColor(), userId));

    }

    private Mono<Desk> getDeskOrThrow(UUID workspaceId, UUID deskId) {
        return deskRepository
                .findByIdAndWorkspaceId(deskId, workspaceId)
                .switchIfEmpty(
                        Mono.error(new EntityNotFoundException(
                                "Доска с id %s не найдена в данном пространстве"
                                        .formatted(deskId.toString())
                        ))
                );
    }
}
