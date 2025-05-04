package org.mrshoffen.tasktracker.task.manager.service;

import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.task.manager.exception.TaskNotFoundException;
import org.mrshoffen.tasktracker.task.manager.exception.TaskStructureException;
import org.mrshoffen.tasktracker.task.manager.model.dto.request.TaskCreateDto;
import org.mrshoffen.tasktracker.task.manager.model.dto.response.TaskResponseDto;
import org.mrshoffen.tasktracker.task.manager.model.entity.Task;
import org.mrshoffen.tasktracker.task.manager.repository.TaskRepository;
import org.mrshoffen.tasktracker.task.manager.util.mapper.TaskMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskMapper taskMapper;

    private final TaskRepository taskRepository;

    public TaskResponseDto createTask(TaskCreateDto taskCreateDto, UUID userId) {
        if (taskCreateDto.mainTaskId() != null) {
            Task task = getUserTaskWithId(taskCreateDto.mainTaskId(), userId);
            validateCorrectParentTaskStructure(task);
        }

        Task task = taskMapper.toEntity(taskCreateDto, userId);
        taskRepository.save(task);

        return taskMapper.toMainTaskDto(task);
    }

    public List<TaskResponseDto> getAllUsersTasks(UUID userId) {
        return taskRepository
                .findUsersRootTasks(userId)
                .stream()
                .map(task -> ((TaskResponseDto) taskMapper.toMainTaskDto(task)))
                .toList();
    }

    @Transactional
    public void deleteTask(UUID taskId, UUID userId) {
        Task task = getUserTaskWithId(taskId, userId);
        taskRepository.delete(task);
    }

    @Transactional
    public void markTaskCompletion(UUID taskId, UUID userId, boolean completed) {
        Task task = getUserTaskWithId(taskId, userId);
        task.setCompleted(completed);
        for (Task subTask : task.getSubtasks()) {
            subTask.setCompleted(completed);
        }
        taskRepository.save(task);
    }

    private void validateCorrectParentTaskStructure(Task parentTask) {
        if (parentTask.getMainTaskId() != null) {
            throw new TaskStructureException("У задачи %s есть родитель. Максимальная вложенность задач - 2"
                    .formatted(parentTask.getId().toString()));
        }
    }

    private Task getUserTaskWithId(UUID taskId, UUID userId) {
        return taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() ->
                        new TaskNotFoundException("Задача с id %s не найдена у пользователя".formatted(taskId.toString()))
                );
    }
}
