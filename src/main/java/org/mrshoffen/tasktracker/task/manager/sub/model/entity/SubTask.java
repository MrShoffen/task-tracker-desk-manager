package org.mrshoffen.tasktracker.task.manager.sub.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UuidGenerator;
import org.mrshoffen.tasktracker.task.manager.main.model.entity.MainTask;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sub_tasks",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"main_task_id", "name"}
                )
        })
public class SubTask {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_task_id", nullable = false, insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MainTask mainTask;

    @Column(name = "main_task_id", nullable = false)
    private UUID mainTaskId; // Для записи

}
