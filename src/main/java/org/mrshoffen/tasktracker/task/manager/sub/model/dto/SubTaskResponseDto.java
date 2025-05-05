package org.mrshoffen.tasktracker.task.manager.sub.model.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;


@Data
public class SubTaskResponseDto {
    private UUID id;
    private String name;
    private String description;
    private String mainTask;
    private Boolean completed;
    private Instant createdAt;
}