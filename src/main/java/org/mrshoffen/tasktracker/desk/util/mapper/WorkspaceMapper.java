package org.mrshoffen.tasktracker.desk.util.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mrshoffen.tasktracker.desk.model.dto.DeskCreateDto;
import org.mrshoffen.tasktracker.desk.model.dto.DeskResponseDto;
import org.mrshoffen.tasktracker.desk.model.entity.Desk;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WorkspaceMapper {

    Desk toDesk(DeskCreateDto deskCreateDto, UUID userId, UUID workspaceId);

    DeskResponseDto toDeskResponse(Desk desk, String taskBoard);


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
