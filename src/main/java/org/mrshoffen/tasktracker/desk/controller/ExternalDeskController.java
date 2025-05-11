package org.mrshoffen.tasktracker.desk.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.commons.web.dto.DeskResponseDto;
import org.mrshoffen.tasktracker.desk.service.DeskService;
import org.mrshoffen.tasktracker.desk.service.PermissionsService;
import org.mrshoffen.tasktracker.desk.model.dto.DeskCreateDto;
import org.mrshoffen.tasktracker.desk.model.dto.OrderIndexUpdateDto;
import org.mrshoffen.tasktracker.desk.model.dto.links.DeskDtoLinksInjector;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mrshoffen.tasktracker.commons.web.authentication.AuthenticationAttributes.AUTHORIZED_USER_HEADER_NAME;
import static org.mrshoffen.tasktracker.commons.web.permissions.Permission.*;

/**
 * Эндпоинты доступные внешнему клиенту (через gateway)
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/workspaces/{workspaceId}/desks")
public class ExternalDeskController {

    private final DeskDtoLinksInjector linksInjector;

    private final DeskService deskService;

    private final PermissionsService permissionsService;

    @PostMapping
    Mono<ResponseEntity<DeskResponseDto>> createDesk(@RequestHeader(AUTHORIZED_USER_HEADER_NAME) UUID userId,
                                                     @Valid @RequestBody Mono<DeskCreateDto> createDto,
                                                     @PathVariable("workspaceId") UUID workspaceId) {
        return permissionsService
                .verifyUserPermission(userId, workspaceId, CREATE_DESK)
                .then(
                        createDto.flatMap(dto ->
                                deskService.createDeskInUserWorkspace(dto, userId, workspaceId))
                )
                .map(linksInjector::injectLinks)
                .map(createdDesk ->
                        ResponseEntity.status(HttpStatus.CREATED)
                                .body(createdDesk)
                );
    }

    @GetMapping
    Flux<DeskResponseDto> getAllDesksInWorkspace(@RequestHeader(AUTHORIZED_USER_HEADER_NAME) UUID userId,
                                                 @PathVariable("workspaceId") UUID workspaceId) {
        return permissionsService
                .verifyUserPermission(userId, workspaceId, READ_WORKSPACE_CONTENT)
                .thenMany(
                        deskService.getAllDesksInUserWorkspace(workspaceId)
                )
                .map(linksInjector::injectLinks);
    }

    @DeleteMapping("/{deskId}")
    Mono<ResponseEntity<Void>> deleteDesk(@RequestHeader(AUTHORIZED_USER_HEADER_NAME) UUID userId,
                                          @PathVariable("workspaceId") UUID workspaceId,
                                          @PathVariable("deskId") UUID deskId) {
        return permissionsService
                .verifyUserPermission(userId, workspaceId, DELETE_DESK)
                .then(
                        deskService.deleteUserDesk(workspaceId, deskId)
                )
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    @PatchMapping("/{deskId}/order")
    Mono<DeskResponseDto> updateDeskOrder(@RequestHeader(AUTHORIZED_USER_HEADER_NAME) UUID userId,
                                          @PathVariable("workspaceId") UUID workspaceId,
                                          @PathVariable("deskId") UUID deskId,
                                          @Valid @RequestBody Mono<OrderIndexUpdateDto> updateDto) {
        return permissionsService
                .verifyUserPermission(userId, workspaceId, UPDATE_DESK)
                .then(
                        updateDto.flatMap(dto ->
                                deskService.updateDeskOrder(workspaceId, deskId, dto)
                        )
                )
                .map(linksInjector::injectLinks);
    }

}


