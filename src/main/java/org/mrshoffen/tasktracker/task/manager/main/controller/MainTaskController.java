package org.mrshoffen.tasktracker.task.manager.main.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.task.manager.main.model.dto.MainTaskCreateDto;
import org.mrshoffen.tasktracker.task.manager.main.model.dto.MainTaskResponseDto;
import org.mrshoffen.tasktracker.task.manager.main.service.MainTaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.mrshoffen.tasktracker.commons.web.authentication.AuthenticationAttributes.AUTHORIZED_USER_HEADER_NAME;

@RequiredArgsConstructor
@RestController
@RequestMapping("/boards/{taskBoardName}")
public class MainTaskController {

    private final MainTaskService taskService;

    @PostMapping
    ResponseEntity<MainTaskResponseDto> createMainTask(@RequestHeader(AUTHORIZED_USER_HEADER_NAME) UUID userId,
                                                       @Valid @RequestBody MainTaskCreateDto createDto,
                                                       @PathVariable("taskBoardName") String boardName) {
        MainTaskResponseDto createdTask = taskService.createMainTask(createDto, userId, boardName);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping
    ResponseEntity<List<MainTaskResponseDto>> getAllMainTasksInBoard(@RequestHeader(AUTHORIZED_USER_HEADER_NAME) UUID userId,
                                                                     @PathVariable("taskBoardName") String boardName) {
        List<MainTaskResponseDto> tasks = taskService.getAllMainTasksInBoard(userId, boardName);
        return ResponseEntity.ok(tasks);
    }
}


