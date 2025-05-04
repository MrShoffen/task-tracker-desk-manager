package org.mrshoffen.tasktracker.task.manager.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.task.manager.model.dto.request.TaskCreateDto;
import org.mrshoffen.tasktracker.task.manager.model.dto.response.TaskResponseDto;
import org.mrshoffen.tasktracker.task.manager.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.mrshoffen.tasktracker.commons.web.authentication.AuthenticationAttributes.AUTHORIZED_USER_HEADER_NAME;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    ResponseEntity<TaskResponseDto> createTask(@RequestHeader(AUTHORIZED_USER_HEADER_NAME) UUID userId,
                                                   @Valid @RequestBody TaskCreateDto createDto) {

        TaskResponseDto task = taskService.createTask(createDto, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping
    ResponseEntity<List<TaskResponseDto>> getTasks(@RequestHeader(AUTHORIZED_USER_HEADER_NAME) UUID userId) {

        List<TaskResponseDto> allUsersTasks = taskService.getAllUsersTasks(userId);
        return ResponseEntity.ok(allUsersTasks);
    }

    @DeleteMapping("{taskId}")
    ResponseEntity<Void> getTask(@RequestHeader(AUTHORIZED_USER_HEADER_NAME) UUID userId,
                                 @PathVariable UUID taskId) {

        taskService.deleteTask(taskId, userId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{taskId}")
    ResponseEntity<Void> markTaskCompletion(@RequestHeader(AUTHORIZED_USER_HEADER_NAME) UUID userId,
                                            @PathVariable UUID taskId,
                                            @RequestParam boolean complete) {

        taskService.markTaskCompletion(taskId, userId, complete);

        return ResponseEntity.ok().build();
    }
}


