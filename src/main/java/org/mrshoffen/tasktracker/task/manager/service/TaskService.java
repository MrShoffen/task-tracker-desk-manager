package org.mrshoffen.tasktracker.task.manager.service;

import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.task.manager.exception.TaskNotFoundException;
import org.mrshoffen.tasktracker.task.manager.exception.TaskStructureException;
import org.mrshoffen.tasktracker.task.manager.model.dto.request.TaskCreateDto;
import org.mrshoffen.tasktracker.task.manager.model.dto.response.TaskParentResponseDto;
import org.mrshoffen.tasktracker.task.manager.model.entity.Task;
import org.mrshoffen.tasktracker.task.manager.repository.TaskRepository;
import org.mrshoffen.tasktracker.task.manager.util.mapper.TaskMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskMapper taskMapper;

    private final TaskRepository taskRepository;

    public TaskParentResponseDto createTask(TaskCreateDto taskCreateDto, UUID userId) {
        if (taskCreateDto.parentTask() != null) {
            Task task = getUserTaskWithId(taskCreateDto.parentTask(), userId);
            validateCorrectParentTaskStructure(task);
        }

        Task task = taskMapper.toEntity(taskCreateDto, userId);
        taskRepository.save(task);

        return taskMapper.toParentDto(task);
    }

    private void validateCorrectParentTaskStructure(Task parentTask) {
        if (parentTask.getParentTask() != null) {
            throw new TaskStructureException("У задачи %s есть родитель. Максимальная вложенность задач - 2"
                    .formatted(parentTask.getId().toString()));
        }
    }

    public List<TaskParentResponseDto> getAllUsersTasks(UUID userId) {
        return taskRepository
                .findUsersRootTasks(userId)
                .stream()
                .map(taskMapper::toParentDto)
                .toList();
    }

    public void deleteTask(UUID taskId, UUID userId) {
        Task task = getUserTaskWithId(taskId, userId);
        taskRepository.delete(task);
    }

    private Task getUserTaskWithId(UUID taskId, UUID userId) {
        return taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() ->
                        new TaskNotFoundException("Задача с id %s не найдена у пользователя".formatted(taskId.toString()))
                );
    }
}
