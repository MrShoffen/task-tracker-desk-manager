package org.mrshoffen.tasktracker.task.manager.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

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

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @JoinColumn(name = "parent_task")
    private UUID parentTask;

    @OneToMany(mappedBy = "parentTask", fetch = FetchType.LAZY)
    private List<Task> subtasks = new ArrayList<>();

}
