package org.mrshoffen.tasktracker.desk.api.internal.controller;


import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.desk.api.internal.service.InternalDeskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Эндпоинты для взаимодействия между микросервисами по бизнес логике
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/internal/workspaces")
public class InternalDeskController {

    private final InternalDeskService deskService;

    /**
     * Необходим для проверки наличия desk у заданного юзера
     */
    @GetMapping("/{userId}/{workspaceId}/desks/{deskId}")
    Mono<Void> ensureUserOwnsDesk(@PathVariable("userId") UUID userId,
                                  @PathVariable("workspaceId") UUID workspaceId,
                                  @PathVariable("deskId") UUID deskId) {
        return deskService
                .ensureUserOwnsDesk(userId, workspaceId, deskId);
    }
}


