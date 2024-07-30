package com.virgo.rekomendasos.service.impl;

import com.virgo.rekomendasos.model.meta.Place;
import com.virgo.rekomendasos.service.GeoApiService;
import com.virgo.rekomendasos.utils.dto.restClientDto.PlaceResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GeoApiServiceImpl implements GeoApiService {

    @Autowired
    private RestClient restClient;

    @Override
    public List<Place> findAll() {
        try {
            String url = UriComponentsBuilder.fromHttpUrl("https://api.geoapify.com/v2/places?")
                    .queryParam("categories", "catering.cafe,commercial.food_and_drink,catering.restaurant")
                    .queryParam("filter", "circle:106.8204823,-6.2976668,10000") // area limit
                    .queryParam("bias", "proximity:106.8204823,-6.2976668")
                    .queryParam("apiKey", "86cdecd1117244e0b55d21a7ba8bdefb")
                    .toUriString();

            var placeResponseDto = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(PlaceResponseDto.class);

            assert placeResponseDto != null;

            return convertToListOfPlace(placeResponseDto);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Place> findAll(Integer limit, String search) {
        try {

            String url = UriComponentsBuilder.fromHttpUrl("https://api.geoapify.com/v2/places?" + (limit == null ? "" : "limit=" + limit) + "&" + (search == null ? "" : "name=" + search))
                    .queryParam("categories", "catering.cafe,commercial.food_and_drink,catering.restaurant")
                    .queryParam("filter", "circle:106.8204823,-6.2976668,10000") // area limit
                    .queryParam("bias", "proximity:106.8204823,-6.2976668")
                    .queryParam("apiKey", "86cdecd1117244e0b55d21a7ba8bdefb")
                    .toUriString();

            var placeResponseDto = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(PlaceResponseDto.class);

            assert placeResponseDto != null;

            return convertToListOfPlace(placeResponseDto);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Optional<Place> findById(String placeId) {

        try {

            String url = UriComponentsBuilder.fromHttpUrl("https://api.geoapify.com/v2/place-details?")
                    .queryParam("id", placeId)
                    .queryParam("apiKey", "86cdecd1117244e0b55d21a7ba8bdefb")
                    .toUriString();

            var placeResponseDto = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(PlaceResponseDto.class);

            assert placeResponseDto != null;

            if (placeResponseDto.getFeatures().isEmpty()) {
                return Optional.empty();
            }

            return Optional.of(Place.builder()
                    .id(placeResponseDto.getFeatures().get(0).getProperties().getPlaceId())
                    .name(placeResponseDto.getFeatures().get(0).getProperties().getName())
                    .latitude(placeResponseDto.getFeatures().get(0).getProperties().getLatitude())
                    .longitude(placeResponseDto.getFeatures().get(0).getProperties().getLongitude())
                    .rating(0)
                    .build());

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Place> convertToListOfPlace(PlaceResponseDto placeResponseDto) {
        List<Place> places = new ArrayList<>();
        List<PlaceResponseDto.Features> features = placeResponseDto.getFeatures();
        for (PlaceResponseDto.Features feature : features) {
            places.add(Place.builder()
                    .id(feature.getProperties().getPlaceId())
                    .name(feature.getProperties().getName())
                    .latitude(feature.getProperties().getLatitude())
                    .longitude(feature.getProperties().getLongitude())
                    .rating(0)
                    .build());
        }
        return places;
    }
}
