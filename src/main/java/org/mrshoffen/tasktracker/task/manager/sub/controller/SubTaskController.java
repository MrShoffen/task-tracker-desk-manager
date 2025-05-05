package org.mrshoffen.tasktracker.task.manager.sub.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.task.manager.main.model.dto.MainTaskCreateDto;
import org.mrshoffen.tasktracker.task.manager.main.model.dto.MainTaskResponseDto;
import org.mrshoffen.tasktracker.task.manager.main.service.MainTaskService;
import org.mrshoffen.tasktracker.task.manager.sub.model.dto.SubTaskCreateDto;
import org.mrshoffen.tasktracker.task.manager.sub.model.dto.SubTaskResponseDto;
import org.mrshoffen.tasktracker.task.manager.sub.service.SubTaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.mrshoffen.tasktracker.commons.web.authentication.AuthenticationAttributes.AUTHORIZED_USER_HEADER_NAME;

@RequiredArgsConstructor
@RestController
@RequestMapping("/boards/{taskBoardName}/{mainTaskName}")
public class SubTaskController {

    private final SubTaskService taskService;

    @PostMapping
    ResponseEntity<SubTaskResponseDto> createSubTask(@RequestHeader(AUTHORIZED_USER_HEADER_NAME) UUID userId,
                                                     @Valid @RequestBody SubTaskCreateDto createDto,
                                                     @PathVariable("taskBoardName") String boardName,
                                                     @PathVariable String mainTaskName) {
        SubTaskResponseDto subTask = taskService.createSubTask(createDto, userId, boardName, mainTaskName);
        return ResponseEntity.status(HttpStatus.CREATED).body(subTask);
    }
//
}


