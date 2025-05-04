package org.mrshoffen.tasktracker.task.manager.model.dto.response;

import lombok.Data;

import java.util.List;


@Data
public class MainTaskResponseDto extends TaskResponseDto {

    private List<SubtaskResponseDto> subtasks;
}