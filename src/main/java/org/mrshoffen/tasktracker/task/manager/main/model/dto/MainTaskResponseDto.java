package org.mrshoffen.tasktracker.task.manager.main.model.dto;

import lombok.Data;
import org.mrshoffen.tasktracker.task.manager.sub.model.dto.SubTaskResponseDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Data
public class MainTaskResponseDto {
    private UUID id;
    private String name;
    private String description;
    private String taskBoard;

    private List<SubTaskResponseDto> subTasks;

    private Boolean completed;
    private Instant createdAt;
}