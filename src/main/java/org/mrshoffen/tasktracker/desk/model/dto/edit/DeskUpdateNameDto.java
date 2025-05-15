package org.mrshoffen.tasktracker.desk.model.dto.edit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

public record DeskUpdateNameDto(

        @Size(max = 256, min = 1, message = "Имя доски должно быть между 1 и 256 символами")
        @NotBlank(message = "Имя доски не может быть пустым")
        String newName) {
}
