package org.mrshoffen.tasktracker.task.manager.model.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class SubtaskResponseDto extends TaskResponseDto {

    private UUID mainTaskId;
}
