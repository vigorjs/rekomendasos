package com.virgo.rekomendasos.repo;

import com.virgo.rekomendasos.model.meta.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, String> {
}
