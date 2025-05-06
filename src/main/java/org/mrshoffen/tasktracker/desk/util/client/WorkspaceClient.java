package org.mrshoffen.tasktracker.desk.util.client;

import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.desk.exception.DeskStructureException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class WorkspaceClient {

    private final WebClient webClient;

    public Mono<UUID> getBoardId(String workspaceName, UUID userId) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/internal/workspaces/id")
                        .queryParam("userId", userId)
                        .queryParam("workspaceName", workspaceName)
                        .build())
                .retrieve()
                .bodyToMono(UUID.class)
                .onErrorMap(WebClientResponseException.NotFound.class, e ->
                        new DeskStructureException("У пользователя нет доски с именем '%s'"
                                .formatted(workspaceName))
                );

    }
}
