package com.virgo.rekomendasos.model.meta;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Data
@Table(name = "places")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Place {
    @Id
    @NotNull
    private String id;

    @Column(name = "name", nullable = false)
    @NotNull
    private String name;

    @Column(name = "latitude", nullable = false)
    @NotNull
    private double latitude;

    @Column(name = "longitude", nullable = false)
    @NotNull
    private double longitude;

    @Column(name = "rating", nullable = false)
    @NotNull
    private Integer rating;

    @OneToMany(mappedBy = "place")
    @JsonIgnore
    private List<Post> posts;
}
