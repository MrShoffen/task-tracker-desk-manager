package org.mrshoffen.tasktracker.desk.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table("desks")
public class Desk {

    @Id
    @Column("id")
    private UUID id;

    @Column("name")
    private String name;

    @Column("created_at")
    private Instant createdAt = Instant.now();

    @Column("order_index")
    private Long orderIndex;

    @Column("user_id")
    private UUID userId;

    @Column("workspace_id")
    private UUID workspaceId;
}
