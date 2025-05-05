package org.mrshoffen.tasktracker.task.manager.sub.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;


public record SubTaskCreateDto(

        @Size(max = 128, min = 3, message = "Имя задачи должно быть между 3 и 128 символами")
        @NotBlank(message = "Имя задачи не может быть пустым")
        String name,

        @Size(max = 500, message = "Максимальное описание задачи - 500 символов")
        String description
) {
}
