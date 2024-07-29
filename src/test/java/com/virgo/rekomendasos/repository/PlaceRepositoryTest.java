package com.virgo.rekomendasos.repository;

import com.virgo.rekomendasos.model.meta.Place;
import com.virgo.rekomendasos.model.meta.Post;
import com.virgo.rekomendasos.repo.PlaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class PlaceRepositoryTest {

    @Autowired
    private PlaceRepository placeRepository;

    @Test
    public void whenCreatePlace_thenReturnCreatedPlace() {
        Place place = Place.builder()
                .id("satu")
                .name("Ragunan")
                .rating(8)
                .latitude(1242.23)
                .longitude(2374.32)
                .posts(new ArrayList<Post>())
                .build();
        placeRepository.save(place);

        Place foundPlace = placeRepository.findById(place.getId()).orElse(null);

        assertThat(foundPlace).isNotNull();
    }

    @Test
    public void whenGetAllPlaces_thenReturnAllPlaces() {
        Place place = Place.builder()
                .id("satu")
                .name("Ragunan")
                .rating(8)
                .latitude(1242.23)
                .longitude(2374.32)
                .posts(new ArrayList<Post>())
                .build();
        placeRepository.save(place);
        Place place2 = Place.builder()
                .id("dua")
                .name("Ragunan 2")
                .rating(8)
                .latitude(1242.23)
                .longitude(2374.32)
                .posts(new ArrayList<Post>())
                .build();
        placeRepository.save(place2);

        List<Place> places = placeRepository.findAll();

        assertThat(places).isNotNull();
        assertThat(places).hasSize(2);
    }

    @Test
    public void whenGetPlaceById_thenReturnPlace() {
        Place place = Place.builder()
                .id("satu")
                .name("Ragunan")
                .rating(8)
                .latitude(1242.23)
                .longitude(2374.32)
                .posts(new ArrayList<Post>())
                .build();
        placeRepository.save(place);

        Place foundPlace = placeRepository.findById(place.getId()).orElse(null);

        assertThat(foundPlace).isNotNull();
    }

    @Test
    public void whenUpdatePlaceById_thenReturnUpdatedPlace() {
        Place place = Place.builder()
                .id("satu")
                .name("Ragunan")
                .rating(8)
                .latitude(1242.23)
                .longitude(2374.32)
                .posts(new ArrayList<Post>())
                .build();
        placeRepository.save(place);

        Place foundPlace = placeRepository.findById(place.getId()).orElse(null);
        assertThat(foundPlace).isNotNull();

        foundPlace.setName("Updated Place");
        Place updatedPlace = placeRepository.save(foundPlace);

        assertThat(updatedPlace).isNotNull();
        assertThat(updatedPlace.getName()).isEqualTo("Updated Place");
    }

    @Test
    public void whenDeletePlaceById_thenPlaceShouldBeDeleted() {
        Place place = Place.builder()
                .id("satu")
                .name("Ragunan")
                .rating(8)
                .latitude(1242.23)
                .longitude(2374.32)
                .posts(new ArrayList<Post>())
                .build();
        placeRepository.save(place);

        placeRepository.deleteById(place.getId());
        Place foundPlace = placeRepository.findById(place.getId()).orElse(null);

        assertThat(foundPlace).isNull();
    }
}
