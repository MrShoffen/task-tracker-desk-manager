package org.mrshoffen.tasktracker.desk.api.internal.service;

import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.commons.web.exception.AccessDeniedException;
import org.mrshoffen.tasktracker.commons.web.exception.EntityNotFoundException;
import org.mrshoffen.tasktracker.desk.repository.DeskRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InternalDeskService {

    private final DeskRepository deskRepository;

    public Mono<Void> ensureUserOwnsDesk(UUID userId, UUID workspaceId, UUID deskId) {
        return deskRepository
                .findByIdAndWorkspaceId(deskId, workspaceId)
                .switchIfEmpty(
                        Mono.error(new EntityNotFoundException(
                                "Доска с id %s не найдена в данном пространстве"
                                        .formatted(deskId.toString())
                        ))
                )
                .flatMap(desk -> {
                    if (desk.getUserId().equals(userId)) {
                        return Mono.empty();
                    } else {
                        return Mono.error(new AccessDeniedException(
                                "Данный пользователь не имеет доступ к данному пространству"
                        ));
                    }
                });
    }


    public Mono<Void> deleteAllUsersDesks(UUID userId, UUID workspaceId) {
        return deskRepository
                .deleteAllByUserIdAndWorkspaceId(userId, workspaceId);
    }
}
