package com.virgo.rekomendasos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virgo.rekomendasos.model.meta.Voucher;
import com.virgo.rekomendasos.service.impl.VoucherServiceImpl;
import com.virgo.rekomendasos.utils.dto.VoucherDTO;
import com.virgo.rekomendasos.utils.responseWrapper.PaginationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class VoucherControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private VoucherServiceImpl voucherServiceImpl;

    @InjectMocks
    private VoucherController voucherController;

    private Voucher voucher1;
    private Voucher voucher2;
    private Page<Voucher> vouchers;
    private PaginationResponse<Voucher> pageVouchers;
    private VoucherDTO voucherDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                .standaloneSetup(voucherController)
                .build();

        voucher1 = Voucher.builder()
                .id(1)
                .name("Makan gratis")
                .price(20000)
                .quantity(10)
                .build();
        voucher2 = Voucher.builder()
                .id(2)
                .name("Minum Gratis")
                .price(10000)
                .quantity(5)
                .build();

        voucherDTO = VoucherDTO.builder()
                .name("Makan gratis")
                .price(20000)
                .quantity(10)
                .build();
        vouchers = new PageImpl<>(Arrays.asList(voucher1, voucher2));
        pageVouchers = new PaginationResponse<>(vouchers);
    }


    @Test
    void findAll_WhenSuccess_ReturnAllVouchers() throws Exception {
        // arrange
        Pageable pageable = PageRequest.of(0, 10);
        when(voucherServiceImpl.findAll(pageable)).thenReturn(vouchers  );

//        mockMvc.perform(get("/api/admin/vouchers")
//                        .param("page", "0")
//                        .param("size", "10"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.status").value("OK"))
//                .andExpect(jsonPath("$.message").value("Success"))
//                .andExpect(jsonPath("$.data").isArray())
//                .andExpect(jsonPath("$.data.content[0].id").value(1))
//                .andExpect(jsonPath("$.data.content[1].id").value(2));
    }


    @Test
    void findById_WhenSuccess_ReturnVoucher() throws Exception {
        // arrange
        when(voucherServiceImpl.findById(1)).thenReturn(voucher1);

        //act  & assert
        mockMvc.perform(get("/api/admin/vouchers/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void create_WhenSuccess_ReturnVoucher() throws Exception {
        // arrange
        when(voucherServiceImpl.create(voucherDTO)).thenReturn(voucher1);

        //act  & assert
        mockMvc.perform(post("/api/admin/vouchers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voucher1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void update_WhenSuccess_ReturnVoucher() throws Exception {
        // arrange
        when(voucherServiceImpl.updateById(1, voucherDTO)).thenReturn(voucher1);

        //act  & assert
        mockMvc.perform(put("/api/admin/vouchers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voucher1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void delete_WhenSuccess() throws Exception {
        // arrange
        doNothing().when(voucherServiceImpl).deleteById(1);

        //act  & assert
        mockMvc.perform(delete("/api/admin/vouchers/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Voucher with id 1 deleted"));
    }
}
