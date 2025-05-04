package org.mrshoffen.tasktracker.task.manager.util.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mrshoffen.tasktracker.task.manager.model.dto.request.TaskCreateDto;
import org.mrshoffen.tasktracker.task.manager.model.dto.response.SubtaskResponseDto;
import org.mrshoffen.tasktracker.task.manager.model.dto.response.MainTaskResponseDto;
import org.mrshoffen.tasktracker.task.manager.model.entity.Task;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "subtasks", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", source = "userId")
    Task toEntity(TaskCreateDto taskCreateDto, UUID userId);

    @Mapping(source = "subtasks", target = "subtasks", qualifiedByName = "subtasks")
    MainTaskResponseDto toMainTaskDto(Task entity);

    @Named("subtasks")
    List<SubtaskResponseDto> toSubtaskDtoList(List<Task> list);

    SubtaskResponseDto toSubtaskDto(Task entity);

}
