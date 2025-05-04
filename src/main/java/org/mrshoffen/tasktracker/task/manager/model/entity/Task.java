package org.mrshoffen.tasktracker.task.manager.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@NamedEntityGraph(
        name = "Task.withSubtasks",
        attributeNodes = @NamedAttributeNode("subtasks")
)
@Entity
@Table(name = "tasks")
public class Task {

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

    @JoinColumn(name = "main_task_id")
    private UUID mainTaskId;

    @OneToMany(mappedBy = "mainTaskId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Task> subtasks = new ArrayList<>();

}
