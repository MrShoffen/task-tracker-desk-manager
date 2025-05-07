package org.mrshoffen.tasktracker.desk.util.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mrshoffen.tasktracker.commons.web.dto.DeskResponseDto;
import org.mrshoffen.tasktracker.desk.model.dto.DeskCreateDto;
import org.mrshoffen.tasktracker.desk.model.entity.Desk;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WorkspaceMapper {

    Desk toDesk(DeskCreateDto deskCreateDto, UUID userId, UUID workspaceId);

    DeskResponseDto toDeskResponse(Desk desk);

}
