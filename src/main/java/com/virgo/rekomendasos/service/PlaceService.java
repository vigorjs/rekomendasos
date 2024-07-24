package com.virgo.rekomendasos.service;

import com.virgo.rekomendasos.model.meta.Place;
import com.virgo.rekomendasos.utils.dto.restClientDto.PlaceResponseDto;

import java.util.List;

public interface PlaceService {
    Place create(Place obj);

    Place findById(String id);

    Place update(String id,Place obj);

    void delete(String id);

    List<Place> findAll(Integer limit); // <7>

    List<Place> findAllPopularPlaces(Integer limit);

    List<Place> getAllPlacesFromApi();

}
