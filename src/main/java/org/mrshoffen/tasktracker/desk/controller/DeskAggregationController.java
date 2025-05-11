package org.mrshoffen.tasktracker.desk.controller;


import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.commons.web.dto.DeskResponseDto;
import org.mrshoffen.tasktracker.desk.service.DeskService;
import org.mrshoffen.tasktracker.desk.model.dto.links.DeskDtoLinksInjector;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Эндпоинты для агрегирующих сервисов.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/aggregate-api/workspaces")
public class DeskAggregationController {

    private final DeskDtoLinksInjector linksInjector;

    private final DeskService deskService;

    /**
     * Необходим для агрегации данных - не требует авторизации и юзера
     */
    @GetMapping("/{workspaceId}/desks")
    Flux<DeskResponseDto> getAllDesksInWorkspace(@PathVariable("workspaceId") UUID workspaceId) {
        return deskService
                .getAllDesksInUserWorkspace(workspaceId)
                .map(linksInjector::injectLinks);
    }

    @GetMapping("/{workspaceId}/desks/{deskId}")
    Mono<DeskResponseDto> getDeskById(@PathVariable("workspaceId") UUID workspaceId,
                                      @PathVariable("deskId") UUID deskId) {
        return deskService
                .getDeskByIdInWorkspace(workspaceId, deskId)
                .map(linksInjector::injectLinks);
    }
}


