package com.virgo.rekomendasos.service;

import com.virgo.rekomendasos.model.meta.Place;
import com.virgo.rekomendasos.repo.PlaceRepository;
import com.virgo.rekomendasos.service.impl.GeoApiServiceImpl;
import com.virgo.rekomendasos.service.impl.PlaceServiceImpl;
import com.virgo.rekomendasos.utils.dto.restClientDto.PlaceResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GeoApiServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @Spy
    @InjectMocks
    private GeoApiServiceImpl geoApiServiceImpl;

    private Place place1;
    private Place place2;
    private Place place3;
    private PlaceResponseDto placeResponseDto;

    @BeforeEach
    void setUp() {
        place1 = Place.builder()
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
        placeResponseDto = PlaceResponseDto.builder().type("ala").build();

    }

    @Test
    void findAll_WhenSuccess_ReturnListOfPlace() {

    }
}
