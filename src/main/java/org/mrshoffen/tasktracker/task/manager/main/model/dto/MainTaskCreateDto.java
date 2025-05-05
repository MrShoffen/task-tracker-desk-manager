package org.mrshoffen.tasktracker.task.manager.main.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record MainTaskCreateDto(

        @Size(max = 128, min = 3, message = "Имя задачи должно быть между 3 и 128 символами")
        @NotBlank(message = "Имя задачи не может быть пустым")
        String name,

        @Size(max = 500, message = "Максимальное описание задачи - 500 символов")
        String description) {
}
