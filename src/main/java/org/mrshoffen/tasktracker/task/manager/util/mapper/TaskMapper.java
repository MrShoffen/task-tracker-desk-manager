package org.mrshoffen.tasktracker.task.manager.util.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mrshoffen.tasktracker.task.manager.model.dto.request.TaskCreateDto;
import org.mrshoffen.tasktracker.task.manager.model.dto.response.TaskChildResponseDto;
import org.mrshoffen.tasktracker.task.manager.model.dto.response.TaskParentResponseDto;
import org.mrshoffen.tasktracker.task.manager.model.entity.Task;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    //    @Mapping(target = "parentTask", ignore = true)
    @Mapping(target = "userId", source = "userId")
    Task toEntity(TaskCreateDto taskCreateDto, UUID userId);

    TaskParentResponseDto toDto(Task entity);

    TaskChildResponseDto toChildTaskDto(Task entity);

    List<TaskChildResponseDto> toChildDtoList(List<Task> list);

}
