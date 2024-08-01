package com.virgo.rekomendasos.service;

import com.virgo.rekomendasos.model.meta.Place;
import com.virgo.rekomendasos.repo.PlaceRepository;
import com.virgo.rekomendasos.service.impl.PlaceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlaceServiceTest {

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private GeoApiService geoApiService;

    @Spy
    @InjectMocks
    private PlaceServiceImpl placeServiceImpl;

    private Place place;
    private Place place2;
    private Place place3;
    private Place placeToUpdate;

    @BeforeEach
    void setUp() {
        place = Place.builder()
                .id("1")
                .name("Museum A")
                .latitude(40.7128)
                .longitude(-74.0060)
                .rating(4)
                .build();

        place2 = Place.builder()
                .id("2")
                .name("Museum B")
                .latitude(34.0522)
                .longitude(-118.2437)
                .rating(3)
                .build();

        place3 = Place.builder()
                .id("3")
                .name("Museum C")
                .latitude(37.7749)
                .longitude(-122.4194)
                .rating(5)
                .build();


        placeToUpdate = Place.builder()
                .id("1")
                .name("Updated Place")
                .latitude(40.7128)
                .longitude(-74.0060)
                .rating(5)
                .build();
    }

    @Test
    void findAll_WhenSuccess_ReturnListOfPlace() {
        when(placeRepository.findAll()).thenReturn(List.of(place, place2));

        List<Place> places = placeServiceImpl.findAll();

        assertNotNull(places);
        assertEquals(2, places.size());
    }

    @Test
    void findById_WhenPlaceFromRepo_ReturnPlace() {
        when(placeRepository.findById("1")).thenReturn(Optional.of(place));

        Place result = placeServiceImpl.findById("1");

        assertEquals(result, place);
    }

    @Test
    void findById_WhenPlaceFromGeoApi_ReturnPlace() {
        when(placeRepository.findById("1")).thenReturn(Optional.empty());
        when(geoApiService.findById("1")).thenReturn(Optional.of(place));

        Place result = placeServiceImpl.findById("1");

        assertEquals(result, place);
    }

    @Test
    void findById_WhenPlaceNotFound_ReturnException() {
        when(placeRepository.findById("1")).thenReturn(Optional.empty());
        when(geoApiService.findById("1")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> placeServiceImpl.findById("1"));

        assertEquals("Place not found", exception.getMessage());
    }

    @Test
    void findById_WhenSuccess_ReturnPlace() {
        when(placeRepository.findById("1")).thenReturn(Optional.of(place));

        Place result = placeServiceImpl.findById("1");

        assertNotNull(result);
        assertEquals(place, result);
    }

    @Test
    void create_WhenSuccess_ReturnPlace() {
        when(placeRepository.save(any(Place.class))).thenReturn(place);

        Place result = placeServiceImpl.create(place);

        assertNotNull(result);
        assertEquals(place, result);
    }

    @Test
    void update_WhenSuccess_ReturnPlace() {
        when(placeRepository.findById("1")).thenReturn(Optional.of(place));
        when(placeRepository.save(any(Place.class))).thenReturn(placeToUpdate);

        Place result = placeServiceImpl.update("1", placeToUpdate);

        assertNotNull(result);
        assertEquals(placeToUpdate, result);
    }

    @Test
    void update_WhenPlaceNotFound_ReturnException() {
        when(placeRepository.findById("1")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> placeServiceImpl.update("1", placeToUpdate));

        assertEquals("Place can not be updated", exception.getMessage());
    }

    @Test
    void deleteByUser_WhenIdNotNull_DeletedPlace() {
        placeServiceImpl.delete("1");

        verify(placeRepository).deleteById("1");
    }

    @Test
    void deleteByUser_WhenIdNull_DoNothing() {
        placeServiceImpl.delete(null);

        verify(placeRepository, never()).deleteById("1");
    }

    @Test
    void findAllPublicPlace_LimitAndSearchNull_ReturnListOfPlace() {
        doReturn(new ArrayList<>(List.of(place, place2))).when(placeServiceImpl).findAll();
        when(geoApiService.findAll()).thenReturn(new ArrayList<>(List.of(place, place3)));

        List<Place> places = placeServiceImpl.findAllPublicPlace(null, null);


        assertNotNull(places);
        assertEquals(3, places.size());
        assertTrue(places.contains(place));
        assertTrue(places.contains(place2));
        assertTrue(places.contains(place3));

        verify(placeServiceImpl).findAll();
        verify(geoApiService).findAll();
        verify(geoApiService, never()).findAll(any(), any());
    }

    @Test
    void findAllPublicPlace_LimitAndSearchNotNull_ReturnListOfPlace() {
        doReturn(new ArrayList<>(List.of(place, place2))).when(placeServiceImpl).findAll();
        when(geoApiService.findAll(any(), any())).thenReturn(new ArrayList<>(List.of(place, place3)));

        List<Place> places = placeServiceImpl.findAllPublicPlace( 2, "museum");

        assertNotNull(places);
        assertEquals(2, places.size());
        assertTrue(places.contains(place));
        assertTrue(places.contains(place2));

        verify(placeServiceImpl).findAll();
        verify(geoApiService).findAll(2, "museum");
        verify(geoApiService, never()).findAll();
    }

    @Test
    void findAllPopularPublicPlaces_LimitAndSearchNull_ReturnListOfPlace() {
        doReturn(new ArrayList<>(List.of(place, place2))).when(placeServiceImpl).findAll();
        when(geoApiService.findAll()).thenReturn(new ArrayList<>(List.of(place, place3)));

        List<Place> places = placeServiceImpl.findAllPopularPublicPlaces(null, null);

        assertNotNull(places);
        assertEquals(3, places.size());
        assertTrue(places.contains(place));
        assertTrue(places.contains(place2));
        assertTrue(places.contains(place3));

        verify(placeServiceImpl).findAll();
        verify(geoApiService).findAll();
        verify(geoApiService, never()).findAll(any(), any());
    }

    @Test
    void findAllPopularPublicPlaces_LimitAndSearchNotNull_ReturnListOfPlace() {
        doReturn(new ArrayList<>(List.of(place, place2))).when(placeServiceImpl).findAll();
        when(geoApiService.findAll(any(), any())).thenReturn(new ArrayList<>(List.of(place, place3)));

        List<Place> places = placeServiceImpl.findAllPopularPublicPlaces( 2, "museum");

        assertNotNull(places);
        assertEquals(2, places.size());
        assertTrue(places.contains(place));
        assertTrue(places.contains(place2));

        verify(placeServiceImpl).findAll();
        verify(geoApiService).findAll(2, "museum");
        verify(geoApiService, never()).findAll();
    }

}
