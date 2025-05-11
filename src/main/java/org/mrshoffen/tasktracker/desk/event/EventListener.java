package org.mrshoffen.tasktracker.desk.event;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mrshoffen.tasktracker.commons.kafka.event.workspace.WorkspaceDeletedEvent;
import org.mrshoffen.tasktracker.desk.service.DeskService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventListener {

    private final DeskService deskService;

    @KafkaListener(topics = WorkspaceDeletedEvent.TOPIC)
    public void handleWorkspaceDeletedEvent(WorkspaceDeletedEvent event) {
        log.info("Received event in topic {} - {}", WorkspaceDeletedEvent.TOPIC, event);
        deskService
                .deleteAllDesksInWorkspace(event.getWorkspaceId())
                .block();
    }


}
