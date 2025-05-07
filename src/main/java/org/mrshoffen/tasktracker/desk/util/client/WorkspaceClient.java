package org.mrshoffen.tasktracker.desk.util.client;

import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.desk.exception.DeskStructureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mrshoffen.tasktracker.commons.web.authentication.AuthenticationAttributes.AUTHORIZED_USER_HEADER_NAME;

@RequiredArgsConstructor
public class WorkspaceClient {

    private final WebClient webClient;

    public Mono<ResponseEntity<Void>> validateWorkspaceStructure(UUID userId, UUID workspaceId) {
        return webClient
                .get()
                .uri("/workspaces/{workspaceId}", workspaceId)
                .header(AUTHORIZED_USER_HEADER_NAME, userId.toString())
                .retrieve()
                .toBodilessEntity()
                .onErrorMap(WebClientResponseException.NotFound.class, e ->
                        new DeskStructureException("У пользователя нет доски с именем '%s'"
                                .formatted(workspaceId))
                );

    }
}
