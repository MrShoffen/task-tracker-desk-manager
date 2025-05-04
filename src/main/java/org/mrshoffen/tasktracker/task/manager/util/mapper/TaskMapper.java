package org.mrshoffen.tasktracker.task.manager.util.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mrshoffen.tasktracker.task.manager.model.dto.request.TaskCreateDto;
import org.mrshoffen.tasktracker.task.manager.model.dto.response.TaskChildResponseDto;
import org.mrshoffen.tasktracker.task.manager.model.dto.response.TaskParentResponseDto;
import org.mrshoffen.tasktracker.task.manager.model.entity.Task;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "subtasks", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", source = "userId")
    Task toEntity(TaskCreateDto taskCreateDto, UUID userId);

    @Mapping(source = "subtasks", target = "subtasks", qualifiedByName = "childTasks")
    TaskParentResponseDto toParentDto(Task entity);

    @Named("childTasks")
    List<TaskChildResponseDto> toChildDtoList(List<Task> list);

    TaskChildResponseDto toChildTaskDto(Task entity);

}
