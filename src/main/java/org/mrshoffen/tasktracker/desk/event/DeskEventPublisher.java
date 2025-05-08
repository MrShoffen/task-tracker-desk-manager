package org.mrshoffen.tasktracker.desk.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mrshoffen.tasktracker.commons.kafka.event.desk.DeskDeletedEvent;
import org.mrshoffen.tasktracker.commons.kafka.event.workspace.WorkspaceDeletedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeskEventPublisher {

    private final KafkaTemplate<UUID, Object> kafkaTemplate;


    public Mono<Void> publishDeskDeletedEvent(UUID userId, UUID workspaceId, UUID deskId) {
        DeskDeletedEvent event = new DeskDeletedEvent(userId, workspaceId, deskId);
        log.info("Event published to kafka topic '{}' - {}", DeskDeletedEvent.TOPIC, event);
        kafkaTemplate.send(DeskDeletedEvent.TOPIC, event.getDeskId(), event);
        return Mono.empty();
    }

}
