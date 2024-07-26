package com.virgo.rekomendasos.service.impl;

import com.virgo.rekomendasos.model.meta.Place;
import com.virgo.rekomendasos.repo.PlaceRepository;
import com.virgo.rekomendasos.service.PlaceService;
import com.virgo.rekomendasos.utils.dto.restClientDto.PlaceResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlaceServiceImpl implements PlaceService {

    private Integer limit = 100;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private RestClient restClient;

    @Override
    public Place create(Place obj) {
        Place place = Place.builder()
                .id(obj.getId())
                .name(obj.getName())
                .latitude(obj.getLatitude())
                .longitude(obj.getLongitude())
                .rating(obj.getRating())
                .build();

        return placeRepository.save(place);
    }

    @Override
    public Place findById(String id) {
        return placeRepository.findById(id).orElseThrow(() -> new RuntimeException("Place not found"));
    }

    @Override
    public Place update(String id, Place obj) {
        Place place = placeRepository.findById(id).orElseThrow(() -> new RuntimeException("Place not found"));
        place.setName(obj.getName());
        place.setLatitude(obj.getLatitude());
        place.setLongitude(obj.getLongitude());
        place.setRating(obj.getRating());

        return placeRepository.save(place);
    }

    @Override
    public void delete(String id) {
        placeRepository.deleteById(id);
    }

    @Override
    public List<Place> findAll(Integer limit) {

        if (limit != null && limit > 0 && limit < 100) {
            this.limit = limit;
        }

        List<Place> places = placeRepository.findAllWithLimit(this.limit);

        var min = this.limit - places.size();
        if (min > 0) {
            List<Place> placeListFromApi = getAllPlacesFromApi();

            for (Place pl : placeListFromApi) {
                if (!places.contains(pl)) {
                    places.add(pl);
                }
            }
        }

        return places;
    }

    @Override
    public List<Place> findAllPopularPlaces(Integer limit) {

        if (limit != null && limit > 0 && limit < 100) {
            this.limit = limit;
        }

        List<Place> sortedPlaces = new ArrayList<>(placeRepository
                .findAll()
                .stream()
                .sorted((p1, p2) -> p2.getRating().compareTo(p1.getRating()))
                .limit(this.limit)
                .toList());

        int min = this.limit - sortedPlaces.size();

        if (min > 0) {
            List<Place> placeListFromApi = getAllPlacesFromApi();

            for (Place pl : placeListFromApi) {
                if (!sortedPlaces.contains(pl)) {
                    sortedPlaces.add(pl);
                }
            }
        }

        return sortedPlaces;
    }

    @Override
    public List<Place> getAllPlacesFromApi() {
        try {
            var placeResponseDto = restClient.get()
                    .uri("https://api.geoapify.com/v2/places?categories=commercial.supermarket&filter=circle:106.8204823,-6.2976668,10000&bias=proximity:106.8204823,-6.2976668&limit=" + (limit.toString()) + "&apiKey=86cdecd1117244e0b55d21a7ba8bdefb")
                    .retrieve()
                    .body(PlaceResponseDto.class);

            assert placeResponseDto != null;
            for (var place : convertToListOfPlace(placeResponseDto)) {
                Place existingPlace = placeRepository.findById(place.getId()).orElse(null);
                if (existingPlace == null) {
                    place.setRating(0);
                    placeRepository.save(place);
                }
            }

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
                    .rating(0)
                    .build());
        }
        return places;
    }

}
