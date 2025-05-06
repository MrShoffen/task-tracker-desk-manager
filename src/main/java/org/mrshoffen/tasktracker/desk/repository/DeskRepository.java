package org.mrshoffen.tasktracker.desk.repository;

import org.mrshoffen.tasktracker.desk.model.entity.Desk;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.Optional;
import java.util.UUID;

public interface DeskRepository extends ReactiveCrudRepository<Desk, UUID> {

    Flux<Desk> findAllByUserIdAndWorkspaceId(UUID userId, UUID taskBoardId);

    Optional<Desk> findByNameAndWorkspaceIdAndUserId(String name, UUID taskBoardId, UUID userId);
}
