package org.mrshoffen.tasktracker.task.manager.sub.service;

import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.task.manager.exception.TaskAlreadyExistsException;
import org.mrshoffen.tasktracker.task.manager.main.model.dto.MainTaskResponseDto;
import org.mrshoffen.tasktracker.task.manager.main.service.MainTaskService;
import org.mrshoffen.tasktracker.task.manager.sub.model.dto.SubTaskCreateDto;
import org.mrshoffen.tasktracker.task.manager.sub.model.dto.SubTaskResponseDto;
import org.mrshoffen.tasktracker.task.manager.sub.model.entity.SubTask;
import org.mrshoffen.tasktracker.task.manager.sub.repository.SubTaskRepository;
import org.mrshoffen.tasktracker.task.manager.util.mapper.TaskMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubTaskService {

    private final MainTaskService mainTaskService;

    private final TaskMapper taskMapper;

    private final SubTaskRepository taskRepository;

    public SubTaskResponseDto createSubTask(SubTaskCreateDto createDto, UUID userId, String boardName, String mainTaskName) {
        UUID parentTask = mainTaskService.getMainTaskId(userId, boardName, mainTaskName);

        SubTask subTask = taskMapper.toSubTask(createDto, parentTask);

        try {
            taskRepository.save(subTask);
        } catch (DataIntegrityViolationException e) {
            throw new TaskAlreadyExistsException(
                    "Подзадача '%s' уже существует в задаче '%s'"
                            .formatted(createDto.name(), mainTaskName));
        }

        return taskMapper.toSubTaskDto(subTask, mainTaskName);
    }


}
