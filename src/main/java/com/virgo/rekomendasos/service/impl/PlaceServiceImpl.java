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
                .rating(obj.getRating() != null ? obj.getRating() : 0)
                .build();

        return placeRepository.save(place);
    }

    @Override
    public Place findById(String id) {
        Place place = placeRepository.findById(id).orElse(null);
        if (place == null) {
            List<Place> placeListFromApi = getAllPlacesFromApi();
            for (Place pl : placeListFromApi) {
                if (pl.getId().equals(id)) {
                    place = pl;
                    break;
                }
            }
        }
        if (place == null) {
            throw new RuntimeException("Place not found");
        }
        return place;
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
    public List<Place> findAll(Integer limit) {

        if (limit != null && limit > 0 && limit < 100) {
            this.limit = limit;
        }

        List<Place> places = placeRepository.findAllWithLimit(this.limit);
        List<Place> newPlaces = new ArrayList<>();

        var min = this.limit - places.size();
        if (min > 0) {
            List<Place> placeListFromApi = getAllPlacesFromApi();

            for (Place pl : placeListFromApi) {
               for (Place p : places) {
                   if (!p.getId().equals(pl.getId())) {
                       newPlaces.add(pl);
                   }
               }
            }
        }

        places.addAll(newPlaces);

        this.limit = 100;

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
        List<Place> newPlaces = new ArrayList<>();

        int min = this.limit - sortedPlaces.size();

        if (min > 0) {
            List<Place> placeListFromApi = getAllPlacesFromApi();

            for (Place pl : placeListFromApi) {
                for (Place p : sortedPlaces) {
                    if (!p.getId().equals(pl.getId())) {
                        newPlaces.add(pl);
                    }
                }
            }
        }

        sortedPlaces.addAll(newPlaces);

        this.limit = 100;

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
