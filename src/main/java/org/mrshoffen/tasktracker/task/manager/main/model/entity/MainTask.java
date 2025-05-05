package org.mrshoffen.tasktracker.task.manager.main.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.mrshoffen.tasktracker.task.manager.sub.model.entity.SubTask;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@NamedEntityGraph(
        name = "MainTask.withSubtasks",
        attributeNodes = @NamedAttributeNode("subTasks")
)
@Entity
@Table(name = "main_tasks",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"task_board_id", "name"}
                )
        })
public class MainTask {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "name", length = 128)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "completed")
    private Boolean completed = false;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "task_board_id", nullable = false)
    private UUID taskBoardId;

    @OneToMany(mappedBy = "mainTask", fetch = FetchType.LAZY)
    List<SubTask> subTasks;

}
