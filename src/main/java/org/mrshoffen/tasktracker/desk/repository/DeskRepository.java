package org.mrshoffen.tasktracker.desk.repository;

import org.mrshoffen.tasktracker.desk.model.entity.Desk;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

public interface DeskRepository extends ReactiveCrudRepository<Desk, UUID> {

    Flux<Desk> findAllByUserIdAndWorkspaceId(UUID userId, UUID workspaceId);

    Mono<Void> deleteAllByUserIdAndWorkspaceId(UUID userId, UUID workspaceId);

    Mono<Desk> findByIdAndWorkspaceId(UUID deskId, UUID workspaceId);

    Flux<Desk> findAllByWorkspaceId(UUID workspaceId);

    Flux<Desk> findAllByWorkspaceIdAndId(UUID workspaceId, UUID deskId);
}
