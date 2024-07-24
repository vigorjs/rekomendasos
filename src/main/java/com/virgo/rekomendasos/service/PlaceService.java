package com.virgo.rekomendasos.service;

import com.virgo.rekomendasos.model.meta.Place;
import com.virgo.rekomendasos.utils.dto.restClientDto.PlaceResponseDto;

import java.util.List;

public interface PlaceService {
    Place create(Place obj);

    List<Place> getAllPlacesFromApi(); // <7>
}
