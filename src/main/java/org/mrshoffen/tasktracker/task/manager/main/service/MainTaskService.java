package org.mrshoffen.tasktracker.task.manager.main.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.task.manager.exception.TaskAlreadyExistsException;
import org.mrshoffen.tasktracker.task.manager.exception.TaskNotFoundException;
import org.mrshoffen.tasktracker.task.manager.exception.TaskStructureException;
import org.mrshoffen.tasktracker.task.manager.main.model.dto.MainTaskCreateDto;
import org.mrshoffen.tasktracker.task.manager.main.model.dto.MainTaskResponseDto;
import org.mrshoffen.tasktracker.task.manager.main.model.entity.MainTask;
import org.mrshoffen.tasktracker.task.manager.main.repository.TaskRepository;


import org.mrshoffen.tasktracker.task.manager.util.client.TaskBoardClient;
import org.mrshoffen.tasktracker.task.manager.util.mapper.TaskMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MainTaskService {

    private final TaskMapper taskMapper;

    private final TaskRepository taskRepository;

    private final TaskBoardClient taskBoardClient;

    public MainTaskResponseDto createMainTask(MainTaskCreateDto taskCreateDto, UUID userId, String boardName) {
        UUID taskDeskId = getTaskBoardId(boardName, userId);
        MainTask mainTask = taskMapper.toMainTask(taskCreateDto, userId, taskDeskId);

        try {
            taskRepository.save(mainTask);
        } catch (DataIntegrityViolationException e) {
            throw new TaskAlreadyExistsException(
                    "Задача с именем '%s' уже существует на доске '%s'"
                            .formatted(taskCreateDto.name(), boardName)
            );
        }
        return taskMapper.toMainTaskDto(mainTask, boardName);
    }


    public UUID getTaskBoardId(String taskBoardName, UUID userId) {
        try {
            String deskId = taskBoardClient.boardId(taskBoardName, userId);
            return UUID.fromString(deskId);
        } catch (FeignException.NotFound e) {
            throw new TaskStructureException(
                    "У пользователя нет доски с именем '%s'"
                            .formatted(taskBoardName));
        }
    }

    public List<MainTaskResponseDto> getAllMainTasksInBoard(UUID userId, String boardName) {
        UUID taskBoardId = getTaskBoardId(boardName, userId);
        return taskRepository.findAllByUserIdAndTaskBoardId(userId, taskBoardId)
                .stream()
                .map(task -> taskMapper.toMainTaskDto(task, boardName))
                .toList();
    }

    public UUID getMainTaskId(UUID userId, String boardName, String mainTaskName) {
        UUID taskBoardId = getTaskBoardId(boardName, userId);
        return taskRepository.findByNameAndTaskBoardIdAndUserId(mainTaskName, taskBoardId, userId)
                .map(MainTask::getId)
                .orElseThrow(
                        () -> new TaskNotFoundException("Задача с именем '%s' не найдена на доске %s"
                                .formatted(mainTaskName, boardName))
                );

    }

//
//    @Transactional
//    public void deleteTask(UUID taskId, UUID userId) {
//        Task task = getUserTaskWithId(taskId, userId);
//        taskRepository.delete(task);
//    }
//
//    @Transactional
//    public void markTaskCompletion(UUID taskId, UUID userId, boolean completed) {
//        Task task = getUserTaskWithId(taskId, userId);
//        task.setCompleted(completed);
//        for (Task subTask : task.getSubtasks()) {
//            subTask.setCompleted(completed);
//        }
//        taskRepository.save(task);
//    }
//
//    private void validateCorrectParentTaskStructure(Task parentTask) {
//        if (parentTask.getMainTaskId() != null) {
//            throw new TaskStructureException("У задачи %s есть родитель. Максимальная вложенность задач - 2"
//                    .formatted(parentTask.getId().toString()));
//        }
//    }
//
//    private Task getUserTaskWithId(UUID taskId, UUID userId) {
//        return taskRepository.findByIdAndUserId(taskId, userId)
//                .orElseThrow(() ->
//                        new TaskNotFoundException("Задача с id %s не найдена у пользователя".formatted(taskId.toString()))
//                );
//    }
}
