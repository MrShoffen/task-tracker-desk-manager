package org.mrshoffen.tasktracker.desk.model.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record DeskCreateDto(
        @Size(max = 128, min = 1, message = "Имя доски должно быть между 1 и 128 символами")
        @NotBlank(message = "Имя доски не может быть пустым")
        String name,

        String color
) {
}
