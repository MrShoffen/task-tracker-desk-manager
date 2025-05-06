package org.mrshoffen.tasktracker.desk.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record DeskCreateDto(

        @Size(max = 128, min = 1, message = "Имя доски должно быть между 3 и 128 символами")
        @NotBlank(message = "Имя доски не может быть пустым")
        String name,

        @Size(max = 500, message = "Максимальное описание доски - 500 символов")
        String description) {
}
