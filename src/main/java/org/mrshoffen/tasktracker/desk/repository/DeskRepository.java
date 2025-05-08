package org.mrshoffen.tasktracker.desk.repository;

import org.mrshoffen.tasktracker.desk.model.entity.Desk;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface DeskRepository extends ReactiveCrudRepository<Desk, UUID> {

    @Query("SELECT COALESCE(MAX(d.order_index), 0) FROM desks d WHERE d.workspace_id = :workspaceId")
    Mono<Long> findMaxOrderIndexInWorkspace(@Param("workspaceId") UUID workspaceId);

    Flux<Desk> findAllByUserIdAndWorkspaceId(UUID userId, UUID workspaceId);

    Mono<Void> deleteAllByUserIdAndWorkspaceId(UUID userId, UUID workspaceId);

    Mono<Desk> findByIdAndWorkspaceId(UUID deskId, UUID workspaceId);

    Flux<Desk> findAllByWorkspaceId(UUID workspaceId);

}
