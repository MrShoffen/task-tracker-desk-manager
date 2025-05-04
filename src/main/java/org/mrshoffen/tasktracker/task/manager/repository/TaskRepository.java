package org.mrshoffen.tasktracker.task.manager.repository;

import org.mrshoffen.tasktracker.task.manager.model.entity.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    Optional<Task> findByIdAndUserId(UUID taskId, UUID userId);

    @EntityGraph(value = "Task.withSubtasks", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT t FROM Task t WHERE t.userId = :userId AND t.parentTask IS NULL")
    List<Task> findUsersRootTasks(@Param("userId") UUID userId);
}
