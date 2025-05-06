package org.mrshoffen.tasktracker.desk.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.desk.model.dto.DeskCreateDto;
import org.mrshoffen.tasktracker.desk.model.dto.DeskResponseDto;
import org.mrshoffen.tasktracker.desk.service.DeskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mrshoffen.tasktracker.commons.web.authentication.AuthenticationAttributes.AUTHORIZED_USER_HEADER_NAME;

@RequiredArgsConstructor
@RestController
@RequestMapping("/workspaces/{workspaceName}/desks")
public class DeskController {

    private final DeskService taskService;

    @PostMapping
    Mono<ResponseEntity<DeskResponseDto>> createDesk(@RequestHeader(AUTHORIZED_USER_HEADER_NAME) UUID userId,
                                                     @Valid @RequestBody Mono<DeskCreateDto> createDto,
                                                     @PathVariable("workspaceName") String workspaceName) {
        return createDto
                .flatMap(dto ->
                        taskService.createDesk(dto, userId, workspaceName)
                )
                .map(createdDesk ->
                        ResponseEntity.status(HttpStatus.CREATED)
                                .body(createdDesk)
                );
    }

    @GetMapping
    Flux<DeskResponseDto> getAllDesksInWorkspace(@RequestHeader(AUTHORIZED_USER_HEADER_NAME) UUID userId,
                                                 @PathVariable("workspaceName") String workspaceName) {
        return taskService
                .getAllDesksInWorkspace(workspaceName, userId);
    }
}


