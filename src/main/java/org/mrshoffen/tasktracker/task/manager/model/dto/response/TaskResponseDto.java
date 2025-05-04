package org.mrshoffen.tasktracker.task.manager.model.dto.response;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class TaskResponseDto {
    private UUID id;
    private String name;
    private String description;
    private Boolean completed;
    private Instant createdAt;
}
