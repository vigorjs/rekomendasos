package com.virgo.rekomendasos.controller;

import com.virgo.rekomendasos.model.meta.Place;
import com.virgo.rekomendasos.service.impl.PlaceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PlaceControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private PlaceServiceImpl placeServiceImpl;

    @InjectMocks
    private PlaceController placeController;

    private Place place1;
    private Place place2;
    private List<Place> places;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                .standaloneSetup(placeController)
                .build();

        place1 = Place.builder().id("place1")
                .name("Laguna")
                .longitude( 123.23)
                .latitude( 123.23)
                .rating(1)
                .build();
        place2 = Place.builder().id("place2")
                .name("Laguna1")
                .longitude(123.33)
                .latitude(123.33)
                .rating(2)
                .build();

        places = new ArrayList<>(Arrays.asList(place1, place2));

    }


    @Test
    void findAll_WhenSuccess_ReturnAllPlaces() throws Exception {
        // arrange
        when(placeServiceImpl.findAll()).thenReturn(places);

        //act  & assert
        mockMvc.perform(get("/api/admin/places"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value("place1"))
                .andExpect(jsonPath("$.data[1].id").value("place2"));
    }


    @Test
    void findById_WhenSuccess_ReturnPlace() throws Exception {
        // arrange
        when(placeServiceImpl.findById("place1")).thenReturn(place1);

        //act  & assert
        mockMvc.perform(get("/api/admin/places/place1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.id").value("place1"));
    }

    @Test
    void create_WhenSuccess_ReturnPlace() throws Exception {
        // arrange
        when(placeServiceImpl.create(place1)).thenReturn(place1);

        //act  & assert
        mockMvc.perform(post("/api/admin/places")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(place1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void update_WhenSuccess_ReturnPlace() throws Exception {
        // arrange
        when(placeServiceImpl.update("place1", place1)).thenReturn(place1);

        //act  & assert
        mockMvc.perform(put("/api/admin/places/place1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(place1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void delete_WhenSuccess() throws Exception {
        // arrange
        doNothing().when(placeServiceImpl).delete("place1");

        //act  & assert
        mockMvc.perform(delete("/api/admin/places/place1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void findAllPlaces_WhenLimitAndSearchAreNull_ReturnAllPlaces() throws Exception {
        // arrange
        when(placeServiceImpl.findAllPublicPlace(null, null)).thenReturn(places);

        //act  & assert
        mockMvc.perform(get("/api/places"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value("place1"))
                .andExpect(jsonPath("$.data[1].id").value("place2"));
    }

    @Test
    void findPopularPlaces_WhenLimitAndSearchAreNull_ReturnAllPlaces() throws Exception {
        // arrange
        when(placeServiceImpl.findAllPopularPublicPlaces(null, null)).thenReturn(places);

        //act  & assert
        mockMvc.perform(get("/api/places/popular"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value("place1"))
                .andExpect(jsonPath("$.data[1].id").value("place2"));
    }
}
