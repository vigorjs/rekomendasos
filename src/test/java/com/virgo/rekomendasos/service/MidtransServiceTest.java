package com.virgo.rekomendasos.service;



import com.virgo.rekomendasos.service.impl.MidtransServiceImpl;
import com.virgo.rekomendasos.utils.dto.restClientDto.MidtransRequestDTO;
import com.virgo.rekomendasos.utils.dto.restClientDto.MidtransResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MidtransServiceTest {
    @Mock
    private RestClient restClient;

    @Mock
    private HttpHeaders headers;

    @InjectMocks
    private MidtransServiceImpl midtransService;

    private String midtransApiUrl = "http://example.com/api/";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        midtransService = new MidtransServiceImpl(restClient, headers);
//        midtransService. = midtransApiUrl;
    }

    @Test
    public void testChargePaymentSuccess() {
        MidtransRequestDTO requestDTO = new MidtransRequestDTO();
        MidtransResponseDTO responseDTO = new MidtransResponseDTO();

        when(restClient.post()
                .uri(eq(midtransApiUrl + "charge"))
                .headers(any())
                .body(eq(requestDTO))
                .retrieve()
                .body(MidtransResponseDTO.class))
                .thenReturn(responseDTO);

        MidtransResponseDTO result = midtransService.chargePayment(requestDTO);

        assertEquals(responseDTO, result);
    }

    @Test
    public void testChargePaymentFailure() {
        MidtransRequestDTO requestDTO = new MidtransRequestDTO();

        when(restClient.post()
                .uri(eq(midtransApiUrl + "charge"))
                .headers(any())
                .body(eq(requestDTO))
                .retrieve()
                .body(MidtransResponseDTO.class))
                .thenThrow(new RuntimeException("Error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            midtransService.chargePayment(requestDTO);
        });

        assertEquals("Error", exception.getMessage());
    }

    @Test
    public void testGetStatusSuccess() {
        String orderId = "12345";
        MidtransResponseDTO responseDTO = new MidtransResponseDTO();

        when(restClient.get()
                .uri(eq(midtransApiUrl + orderId + "/status"))
                .headers(any())
                .retrieve()
                .body(MidtransResponseDTO.class))
                .thenReturn(responseDTO);

        MidtransResponseDTO result = midtransService.getStatus(orderId);

        assertEquals(responseDTO, result);
    }

    @Test
    public void testGetStatusFailure() {
        String orderId = "12345";

        when(restClient.get()
                .uri(eq(midtransApiUrl + orderId + "/status"))
                .headers(any())
                .retrieve()
                .body(MidtransResponseDTO.class))
                .thenThrow(new RuntimeException("Error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            midtransService.getStatus(orderId);
        });

        assertEquals("Error", exception.getMessage());
    }

    @Test
    public void testChangeStatusSuccess() {
        String orderId = "12345";
        String status = "cancel";
        MidtransResponseDTO responseDTO = new MidtransResponseDTO();

        when(restClient.post()
                .uri(eq(midtransApiUrl + orderId + "/" + status))
                .headers(any())
                .retrieve()
                .body(MidtransResponseDTO.class))
                .thenReturn(responseDTO);

        MidtransResponseDTO result = midtransService.changeStatus(orderId, status);

        assertEquals(responseDTO, result);
    }

    @Test
    public void testChangeStatusFailure() {
        String orderId = "12345";
        String status = "cancel";

        when(restClient.post()
                .uri(eq(midtransApiUrl + orderId + "/" + status))
                .headers(any())
                .retrieve()
                .body(MidtransResponseDTO.class))
                .thenThrow(new RuntimeException("Error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            midtransService.changeStatus(orderId, status);
        });

        assertEquals("Error", exception.getMessage());
    }
}
