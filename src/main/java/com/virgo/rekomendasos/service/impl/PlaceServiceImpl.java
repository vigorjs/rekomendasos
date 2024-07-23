package com.virgo.rekomendasos.service.impl;

import com.virgo.rekomendasos.model.meta.Place;
import com.virgo.rekomendasos.service.PlaceService;
import com.virgo.rekomendasos.utils.dto.restClientDto.PlaceResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlaceServiceImpl implements PlaceService {

    @Autowired
    private RestClient restClient;

    @Override
    public Place create(Place obj) {
        return Place.builder()
                .id(obj.getId())
                .name(obj.getName())
                .latitude(obj.getLatitude())
                .longitude(obj.getLongitude())
                .build();
    }

    @Override
    public List<Place> getAllPlacesFromApi() {
        try {
            var placeResponseDto = restClient.get()
                    .uri("https://api.geoapify.com/v2/places?categories=commercial.supermarket&filter=circle:106.8204823,-6.2976668,5000&bias=proximity:106.8204823,-6.2976668&limit=50&apiKey=86cdecd1117244e0b55d21a7ba8bdefb")
                    .retrieve()
                    .body(PlaceResponseDto.class);

            assert placeResponseDto != null;
            return convertToListOfPlace(placeResponseDto);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Place> convertToListOfPlace(PlaceResponseDto placeResponseDto) {
        List<Place> places = new ArrayList<>();
        List<PlaceResponseDto.Features> features = placeResponseDto.getFeatures();
        for (PlaceResponseDto.Features feature : features) {
            places.add(Place.builder()
                    .id(feature.getProperties().getPlaceId())
                    .name(feature.getProperties().getName())
                    .latitude(feature.getProperties().getLatitude())
                    .longitude(feature.getProperties().getLongitude())
                    .build());
        }
        return places;
    }

}
