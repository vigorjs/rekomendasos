package com.virgo.rekomendasos.model.meta;

import jakarta.persistence.*;
import lombok.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "longitude", nullable = false)
    private double longitude;
}
