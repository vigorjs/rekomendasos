package com.virgo.rekomendasos.service;

import com.virgo.rekomendasos.model.meta.Place;
import com.virgo.rekomendasos.repo.PlaceRepository;
import com.virgo.rekomendasos.service.impl.PlaceServiceImpl;
import com.virgo.rekomendasos.utils.dto.restClientDto.PlaceResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class PlaceServiceTest {

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private PlaceServiceImpl placeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup RestClient chain
        doReturn(requestHeadersUriSpec).when(restClient).get();
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(anyString());
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();
    }

    @Test
    void testCreate_Success() {
        Place inputPlace = Place.builder()
                .id("1")
                .name("Test Place")
                .latitude(1.0)
                .longitude(1.0)
                .rating(5)
                .build();

        when(placeRepository.save(any(Place.class))).thenReturn(inputPlace);

        Place result = placeService.create(inputPlace);

        assertNotNull(result);
        assertEquals(inputPlace.getId(), result.getId());
        assertEquals(inputPlace.getName(), result.getName());
        assertEquals(inputPlace.getLatitude(), result.getLatitude());
        assertEquals(inputPlace.getLongitude(), result.getLongitude());
        assertEquals(inputPlace.getRating(), result.getRating());

        verify(placeRepository, times(1)).save(any(Place.class));
    }

    @Test
    void testFindById_ExistingPlace() {
        String placeId = "1";
        Place existingPlace = Place.builder().id(placeId).name("Existing Place").build();

        when(placeRepository.findById(placeId)).thenReturn(Optional.of(existingPlace));

        Place result = placeService.findById(placeId);

        assertNotNull(result);
        assertEquals(placeId, result.getId());
        assertEquals("Existing Place", result.getName());

        verify(placeRepository, times(1)).findById(placeId);
        verify(restClient, never()).get();
    }

    @Test
    void testFindById_PlaceFromApi() {
        String placeId = "2";
        Place apiPlace = Place.builder().id(placeId).name("API Place").build();

        when(placeRepository.findById(placeId)).thenReturn(Optional.empty());
        doReturn(createMockPlaceResponseDto(Arrays.asList(apiPlace)))
                .when(responseSpec).body(PlaceResponseDto.class);

        Place result = placeService.findById(placeId);

        assertNotNull(result);
        assertEquals(placeId, result.getId());
        assertEquals("API Place", result.getName());

        verify(placeRepository, times(1)).findById(placeId);
        verify(restClient, times(1)).get();
    }

    @Test
    void testFindById_NotFound() {
        String placeId = "3";

        when(placeRepository.findById(placeId)).thenReturn(Optional.empty());
        doReturn(createMockPlaceResponseDto(Arrays.asList()))
                .when(responseSpec).body(PlaceResponseDto.class);

        assertThrows(RuntimeException.class, () -> placeService.findById(placeId));

        verify(placeRepository, times(1)).findById(placeId);
        verify(restClient, times(1)).get();
    }

    @Test
    void testUpdate_Success() {
        String placeId = "1";
        Place existingPlace = Place.builder().id(placeId).name("Old Name").build();
        Place updatedPlace = Place.builder().id(placeId).name("New Name").build();

        when(placeRepository.findById(placeId)).thenReturn(Optional.of(existingPlace));
        when(placeRepository.save(any(Place.class))).thenReturn(updatedPlace);

        Place result = placeService.update(placeId, updatedPlace);

        assertNotNull(result);
        assertEquals(placeId, result.getId());
        assertEquals("New Name", result.getName());

        verify(placeRepository, times(1)).findById(placeId);
        verify(placeRepository, times(1)).save(any(Place.class));
    }

    @Test
    void testUpdate_NotFound() {
        String placeId = "1";
        Place updatedPlace = Place.builder().id(placeId).name("New Name").build();

        when(placeRepository.findById(placeId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> placeService.update(placeId, updatedPlace));

        verify(placeRepository, times(1)).findById(placeId);
        verify(placeRepository, never()).save(any(Place.class));
    }

    @Test
    void testDelete() {
        String placeId = "1";

        placeService.delete(placeId);

        verify(placeRepository, times(1)).deleteById(placeId);
    }

    @Test
    void testFindAll_WithinLimit() {
        List<Place> places = Arrays.asList(
                Place.builder().id("1").name("Place 1").build(),
                Place.builder().id("2").name("Place 2").build()
        );

        when(placeRepository.findAllWithLimit(100)).thenReturn(places);

        List<Place> result = placeService.findAll(null);

        assertEquals(2, result.size());
        verify(placeRepository, times(1)).findAllWithLimit(100);
        verify(restClient, never()).get();
    }

    @Test
    void testFindAll_ExceedingLimit() {
        List<Place> dbPlaces = Arrays.asList(
                Place.builder().id("1").name("Place 1").build()
        );
        List<Place> apiPlaces = Arrays.asList(
                Place.builder().id("2").name("Place 2").build(),
                Place.builder().id("3").name("Place 3").build()
        );

        when(placeRepository.findAllWithLimit(100)).thenReturn(dbPlaces);
        doReturn(createMockPlaceResponseDto(apiPlaces))
                .when(responseSpec).body(PlaceResponseDto.class);

        List<Place> result = placeService.findAll(null);

        assertEquals(3, result.size());
        verify(placeRepository, times(1)).findAllWithLimit(100);
        verify(restClient, times(1)).get();
    }

    @Test
    void testFindAllPopularPlaces() {
        List<Place> places = Arrays.asList(
                Place.builder().id("1").name("Place 1").rating(5).build(),
                Place.builder().id("2").name("Place 2").rating(4).build(),
                Place.builder().id("3").name("Place 3").rating(3).build()
        );

        when(placeRepository.findAll()).thenReturn(places);

        List<Place> result = placeService.findAllPopularPlaces(null);

        assertEquals(3, result.size());
        assertEquals("Place 1", result.get(0).getName());
        assertEquals("Place 2", result.get(1).getName());
        assertEquals("Place 3", result.get(2).getName());

        verify(placeRepository, times(1)).findAll();
    }

    @Test
    void testGetAllPlacesFromApi() {
        List<Place> apiPlaces = Arrays.asList(
                Place.builder().id("1").name("API Place 1").build(),
                Place.builder().id("2").name("API Place 2").build()
        );

        doReturn(createMockPlaceResponseDto(apiPlaces))
                .when(responseSpec).body(PlaceResponseDto.class);

        List<Place> result = placeService.getAllPlacesFromApi();

        assertEquals(2, result.size());
        assertEquals("API Place 1", result.get(0).getName());
        assertEquals("API Place 2", result.get(1).getName());

        verify(restClient, times(1)).get();
    }

    private PlaceResponseDto createMockPlaceResponseDto(List<Place> places) {
        PlaceResponseDto responseDto = new PlaceResponseDto();
        List<PlaceResponseDto.Features> features = places.stream()
                .map(place -> {
                    PlaceResponseDto.Features.Properties properties = PlaceResponseDto.Features.Properties.builder()
                            .placeId(place.getId())
                            .name(place.getName())
                            .latitude(place.getLatitude())
                            .longitude(place.getLongitude())
                            .build();
                    return PlaceResponseDto.Features.builder().properties(properties).build();
                })
                .toList();
        responseDto.setFeatures(features);
        return responseDto;
    }
}