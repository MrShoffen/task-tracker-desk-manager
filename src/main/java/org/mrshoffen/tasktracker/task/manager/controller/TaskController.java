package org.mrshoffen.tasktracker.task.manager.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.task.manager.model.dto.request.TaskCreateDto;
import org.mrshoffen.tasktracker.task.manager.model.dto.response.TaskParentResponseDto;
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
    ResponseEntity<TaskParentResponseDto> createTask(@RequestHeader(AUTHORIZED_USER_HEADER_NAME) UUID userId,
                                                     @Valid @RequestBody TaskCreateDto createDto) {

        TaskParentResponseDto task = taskService.createTask(createDto, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping
    ResponseEntity<List<TaskParentResponseDto>> getTasks(@RequestHeader(AUTHORIZED_USER_HEADER_NAME) UUID userId) {

        List<TaskParentResponseDto> allUsersTasks = taskService.getAllUsersTasks(userId);
        return ResponseEntity.ok(allUsersTasks);
    }

    @DeleteMapping("{taskId}")
    ResponseEntity<Void> getTask(@RequestHeader(AUTHORIZED_USER_HEADER_NAME) UUID userId,
                                 @PathVariable UUID taskId) {

        taskService.deleteTask(taskId, userId);

        return ResponseEntity.noContent().build();
    }
}


