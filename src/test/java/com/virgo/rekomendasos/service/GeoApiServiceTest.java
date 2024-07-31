package com.virgo.rekomendasos.service;

import com.virgo.rekomendasos.service.impl.GeoApiServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

@ExtendWith(MockitoExtension.class)
public class GeoApiServiceTest {

    @Mock
    private RestClient restClient;

    @InjectMocks
    private GeoApiServiceImpl geoApiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_WhenSuccess() {
    }

    @Test
    void findAll_WhenSuccessWithParam() {
    }

    @Test
    void findById_WhenSuccess() {
    }
}
