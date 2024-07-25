package com.virgo.rekomendasos.repo;

import com.virgo.rekomendasos.model.meta.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, String> {

    @Query(value = "SELECT * FROM places LIMIT :limit", nativeQuery = true)
    List<Place> findAllWithLimit( Integer limit);
}
