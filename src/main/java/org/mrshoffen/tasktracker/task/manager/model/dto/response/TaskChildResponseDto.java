package org.mrshoffen.tasktracker.task.manager.model.dto.response;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class TaskChildResponseDto {
    private UUID id;
    private String name;
    private String description;
    private Boolean completed;
    private Instant createdAt;
    private UUID parentTask;
}
