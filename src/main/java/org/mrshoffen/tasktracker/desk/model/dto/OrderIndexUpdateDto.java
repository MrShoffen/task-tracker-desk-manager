package org.mrshoffen.tasktracker.desk.model.dto;

import jakarta.validation.constraints.NotNull;

public record OrderIndexUpdateDto(
        @NotNull(message = "Новый индекс не может быть пустым")
        Long updatedIndex
) {
}
