package org.mrshoffen.tasktracker.task.manager.main.repository;

import org.mrshoffen.tasktracker.task.manager.main.model.entity.MainTask;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<MainTask, UUID> {

    @EntityGraph(value = "MainTask.withSubtasks", type = EntityGraph.EntityGraphType.FETCH)
    List<MainTask> findAllByUserIdAndTaskBoardId(UUID userId, UUID taskBoardId);

    Optional<MainTask> findByNameAndTaskBoardIdAndUserId(String name, UUID taskBoardId, UUID userId);
}
