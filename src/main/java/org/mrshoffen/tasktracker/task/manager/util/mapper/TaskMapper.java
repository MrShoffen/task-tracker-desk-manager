package org.mrshoffen.tasktracker.task.manager.util.mapper;


import org.mapstruct.*;
import org.mrshoffen.tasktracker.task.manager.main.model.dto.MainTaskCreateDto;
import org.mrshoffen.tasktracker.task.manager.main.model.dto.MainTaskResponseDto;
import org.mrshoffen.tasktracker.task.manager.main.model.entity.MainTask;
import org.mrshoffen.tasktracker.task.manager.sub.model.dto.SubTaskCreateDto;
import org.mrshoffen.tasktracker.task.manager.sub.model.dto.SubTaskResponseDto;
import org.mrshoffen.tasktracker.task.manager.sub.model.entity.SubTask;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {

    MainTask toMainTask(MainTaskCreateDto mainTaskCreateDto, UUID userId, UUID taskBoardId);

    @Mapping(target = "subTasks", expression = "java(toSubTaskDtoList(mainTask.getSubTasks(),mainTask.getName()))")
    MainTaskResponseDto toMainTaskDto(MainTask mainTask, String taskBoard);

    SubTask toSubTask(SubTaskCreateDto subTaskCreateDto, UUID mainTaskId);

    @Mapping(target = "mainTask", source = "mainTaskName")
    SubTaskResponseDto toSubTaskDto(SubTask subTask, String mainTaskName);

    default List<SubTaskResponseDto> toSubTaskDtoList(List<SubTask> subTasks, String mainTaskName) {
        return subTasks.stream()
                .map(subTask -> toSubTaskDto(subTask, mainTaskName))
                .toList();
    }

//    List<SubTask> toSubTaskList(List<SubTask> subTaskList, String taskBoard);
//    @Mapping(target = "subtasks", ignore = true)
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "userId", source = "userId")
//    Task toEntity(TaskCreateDto taskCreateDto, UUID userId);
//
//    @Mapping(source = "subtasks", target = "subtasks", qualifiedByName = "subtasks")
//    MainTaskResponseDto toMainTaskDto(Task entity);
//
//    @Named("subtasks")
//    List<SubtaskResponseDto> toSubtaskDtoList(List<Task> list);
//
//    SubtaskResponseDto toSubtaskDto(Task entity);

}
