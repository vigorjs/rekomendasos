package com.virgo.todoapp.entity.meta;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.virgo.todoapp.entity.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@Table(name = "Tasks")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Task{

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "Name")
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "Deadline", nullable = true)
    private Date deadline;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "userId")
    private User user;


}
