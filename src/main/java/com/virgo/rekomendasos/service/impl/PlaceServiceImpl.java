package com.virgo.rekomendasos.service.impl;

import com.virgo.rekomendasos.model.meta.Place;
import com.virgo.rekomendasos.repo.PlaceRepository;
import com.virgo.rekomendasos.service.GeoApiService;
import com.virgo.rekomendasos.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PlaceServiceImpl implements PlaceService {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private GeoApiService geoApiService;

    @Override
    public List<Place> findAll() {
        return placeRepository.findAll();
    }

    @Override
    public Place findById(String id) {
        Place place = placeRepository.findById(id).orElse(null);

        if (place == null) {
            place = geoApiService.findById(id).orElseThrow(() -> new RuntimeException("Place not found"));
        }

        return place;
    }

    @Override
    public Place create(Place obj) {
        Place place = Place.builder()
                .id(obj.getId())
                .name(obj.getName())
                .latitude(obj.getLatitude())
                .longitude(obj.getLongitude())
                .rating(obj.getRating() != null ? obj.getRating() : 0)
                .build();

        return placeRepository.save(place);
    }

    @Override
    public Place update(String id, Place obj) {
        Place place = placeRepository.findById(id).orElseThrow(() -> new RuntimeException("Place can not be updated"));
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
    public List<Place> findAllPublicPlace(Integer limit, String search) {

        List<Place> places = findAll();
        List<Place> placeListFromApi;

        if (limit == null && search == null) {
            placeListFromApi = geoApiService.findAll();
        } else {
            placeListFromApi = geoApiService.findAll(limit, search);
        }


        Set<String> existingPlaceIds = places.stream()
                .map(Place::getId)
                .collect(Collectors.toSet());


        List<Place> newPlaces = placeListFromApi.stream()
                .filter(place -> !existingPlaceIds.contains(place.getId()))
                .toList();

        places.addAll(newPlaces);

        if (search != null) {
            places = places.stream()
                    .filter((place) -> Pattern.matches(".*" + Pattern.quote(search.toLowerCase()) + ".*", place.getName().toLowerCase()))
                    .toList();
        }

        if (limit != null) {
            places = places.stream()
                    .limit(limit)
                    .toList();
        }

        return places;
    }

    @Override
    public List<Place> findAllPopularPublicPlaces(Integer limit, String search) {

        List<Place> sortedPlaces = new ArrayList<>(
                findAll()
                .stream()
                .sorted((p1, p2) -> p2.getRating().compareTo(p1.getRating()))
                .toList());

        List<Place> placeListFromApi;

        if ( limit == null && search == null) {
            placeListFromApi = geoApiService.findAll();
        } else {
            placeListFromApi = geoApiService.findAll(limit, search);
        }

        Set<String> existingPlaceIds = sortedPlaces.stream()
                .map(Place::getId)
                .collect(Collectors.toSet());


        List<Place> newPlaces = placeListFromApi.stream()
                .filter(place -> !existingPlaceIds.contains(place.getId()))
                .toList();

        sortedPlaces.addAll(newPlaces);

        if (search != null) {
            sortedPlaces = sortedPlaces.stream()
                    .filter((place) -> Pattern.matches(".*" + Pattern.quote(search.toLowerCase()) + ".*", place.getName().toLowerCase()))
                    .toList();
        }

        if (limit != null) {
            sortedPlaces = sortedPlaces.stream()
                    .limit(limit)
                    .toList();
        }

        return sortedPlaces;
    }

}
