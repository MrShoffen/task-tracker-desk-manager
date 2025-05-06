package org.mrshoffen.tasktracker.desk.model.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;


@Data
public class DeskResponseDto {
    private UUID id;
    private String name;
    private String description;
    private String taskBoard;
    private Instant createdAt;
}