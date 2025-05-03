package org.mrshoffen.tasktracker.task.manager.model.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class TaskChildResponseDto {
    private UUID id;
    private String name;
    private String description;
    private UUID parentTask;
}
