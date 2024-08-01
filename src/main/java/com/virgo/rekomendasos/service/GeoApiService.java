package com.virgo.rekomendasos.service;

import com.virgo.rekomendasos.model.meta.Place;

import java.util.List;
import java.util.Optional;

public interface GeoApiService {

    List<Place> findAll();

    List<Place> findAll(Integer limit, String search);

    Optional<Place> findById(String id);

}
