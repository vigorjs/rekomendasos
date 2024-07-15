package com.virgo.todoapp.entity.meta;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "Categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Category {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "Name", unique = true)
    private String name;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "userId")
    private User user;

}
