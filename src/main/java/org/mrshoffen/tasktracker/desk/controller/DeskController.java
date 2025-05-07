package org.mrshoffen.tasktracker.desk.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.commons.utils.HateoasLinks;
import org.mrshoffen.tasktracker.commons.web.dto.DeskResponseDto;
import org.mrshoffen.tasktracker.commons.web.dto.WorkspaceResponseDto;
import org.mrshoffen.tasktracker.desk.model.dto.DeskCreateDto;
import org.mrshoffen.tasktracker.desk.service.DeskService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mrshoffen.tasktracker.commons.web.authentication.AuthenticationAttributes.AUTHORIZED_USER_HEADER_NAME;

@RequiredArgsConstructor
@RestController
@RequestMapping("/workspaces/{workspaceId}/desks")
public class DeskController {

    @Value("${app.gateway.api-prefix}")
    private String apiPrefix;

    private final DeskService deskService;

    @PostMapping
    Mono<ResponseEntity<DeskResponseDto>> createDesk(@RequestHeader(AUTHORIZED_USER_HEADER_NAME) UUID userId,
                                                     @Valid @RequestBody Mono<DeskCreateDto> createDto,
                                                     @PathVariable("workspaceId") UUID workspaceId) {
        return createDto
                .flatMap(dto ->
                        deskService.createDesk(dto, userId, workspaceId)
                )
                .map(this::addLinks)
                .map(createdDesk ->
                        ResponseEntity.status(HttpStatus.CREATED)
                                .body(createdDesk)
                );
    }

    @GetMapping
    Flux<DeskResponseDto> getAllDesksInWorkspace(@RequestHeader(AUTHORIZED_USER_HEADER_NAME) UUID userId,
                                                 @PathVariable("workspaceId") UUID workspaceId) {
        return deskService
                .getAllDesksInWorkspace(workspaceId, userId)
                .map(this::addLinks);
    }

    @GetMapping("/{deskId}")
    Mono<DeskResponseDto> getDeskById(@RequestHeader(AUTHORIZED_USER_HEADER_NAME) UUID userId,
                                      @PathVariable("workspaceId") UUID workspaceId,
                                      @PathVariable("deskId") UUID deskId) {
        return deskService
                .getUserDeskInWorkspace(userId, workspaceId, deskId)
                .map(this::addLinks);
    }

    public DeskResponseDto addLinks(DeskResponseDto dto) {
        HateoasLinks links = HateoasLinks.builder()
                .setPrefix(apiPrefix) // todo move to config server
                .addLink("allTasks",
                        "/workspaces/%s/desks/%s/tasks"
                                .formatted(dto.getWorkspaceId(), dto.getId()),
                        "GET")
                .addLink("createTask",
                        "/workspaces/%s/desks/%s/tasks"
                                .formatted(dto.getWorkspaceId(), dto.getId()),
                        "POST")
                .addLink("allDesks",
                        "/workspaces/%s/desks"
                                .formatted(dto.getWorkspaceId()),
                        "GET")
                .addLink("createDesk",
                        "/workspaces/%s/desks"
                                .formatted(dto.getWorkspaceId()),
                        "POST")
                .addLink("self",
                        "/workspaces/%s/desks/%s"
                                .formatted(dto.getWorkspaceId(), dto.getId()),
                        "GET")
                .addLink("allWorkspaces",
                        "/workspaces",
                        "GET")
                .addLink("createWorkspace",
                        "/workspaces",
                        "POST")
                .build();

        dto.setApi(links);
        return dto;
    }
}


