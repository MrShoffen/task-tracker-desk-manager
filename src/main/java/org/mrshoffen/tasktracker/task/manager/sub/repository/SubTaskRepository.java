package org.mrshoffen.tasktracker.task.manager.sub.repository;

import org.mrshoffen.tasktracker.task.manager.main.model.entity.MainTask;
import org.mrshoffen.tasktracker.task.manager.sub.model.entity.SubTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubTaskRepository extends JpaRepository<SubTask, UUID> {
}
