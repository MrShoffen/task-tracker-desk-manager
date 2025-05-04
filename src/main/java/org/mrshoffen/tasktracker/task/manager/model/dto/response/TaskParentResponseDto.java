package org.mrshoffen.tasktracker.task.manager.model.dto.response;

import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Data
public class TaskParentResponseDto {
    private UUID id;
    private String name;
    private String description;
    private Boolean completed;
    private Instant createdAt;
    private List<TaskChildResponseDto> subtasks;
}