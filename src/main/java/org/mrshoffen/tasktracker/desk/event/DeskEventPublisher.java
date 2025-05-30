package org.mrshoffen.tasktracker.desk.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mrshoffen.tasktracker.commons.kafka.event.desk.DeskCreatedEvent;
import org.mrshoffen.tasktracker.commons.kafka.event.desk.DeskDeletedEvent;
import org.mrshoffen.tasktracker.commons.kafka.event.desk.DeskUpdatedEvent;
import org.mrshoffen.tasktracker.commons.kafka.event.workspace.WorkspaceDeletedEvent;
import org.mrshoffen.tasktracker.commons.web.dto.DeskResponseDto;
import org.mrshoffen.tasktracker.desk.model.entity.Desk;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeskEventPublisher {

    private final KafkaTemplate<UUID, Object> kafkaTemplate;


    public void publishDeskDeletedEvent(Desk desk) {
        DeskDeletedEvent event = new DeskDeletedEvent(desk.getUserId(), desk.getWorkspaceId(), desk.getId(), Instant.now());
        log.info("Event published to kafka topic '{}' - {}", DeskDeletedEvent.TOPIC, event);
        kafkaTemplate.send(DeskDeletedEvent.TOPIC, event.getDeskId(), event);
    }

    public void publishDeskCreatedEvent(DeskResponseDto desk) {
        DeskCreatedEvent event = new DeskCreatedEvent(desk);
        log.info("Event published to kafka topic '{}' - {}", DeskCreatedEvent.TOPIC, event);
        kafkaTemplate.send(DeskCreatedEvent.TOPIC, desk.getId(), event);
    }

    public void publishDeskUpdatedEvent(UUID workspaceId, UUID deskId, String fieldName, Object newValue, UUID updatedBy) {
        DeskUpdatedEvent event = DeskUpdatedEvent.builder()
                .workspaceId(workspaceId)
                .deskId(deskId)
                .updatedBy(updatedBy)
                .updatedAt(Instant.now())
                .updatedField(Map.of(fieldName, newValue))
                .build();
        log.info("Event published to kafka topic '{}' - {}", DeskUpdatedEvent.TOPIC, event);
        kafkaTemplate.send(DeskUpdatedEvent.TOPIC, deskId, event);

    }

}
