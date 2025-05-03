package org.mrshoffen.tasktracker.task.manager.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;


public record TaskCreateDto(

        @Size(max = 128, min = 3, message = "Имя задачи должно быть между 3 и 128 символами")
        @NotNull(message = "Имя задачи не может быть пустым")
        String name,

        @Size(max = 500, message = "Максимальное описание задачи - 500 символов")
        String description,

        UUID parentTask) {
}
