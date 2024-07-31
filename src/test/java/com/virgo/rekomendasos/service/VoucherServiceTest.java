package com.virgo.rekomendasos.service;

import com.virgo.rekomendasos.model.meta.Voucher;
import com.virgo.rekomendasos.model.meta.User;
import com.virgo.rekomendasos.model.meta.VoucherTransaction;
import com.virgo.rekomendasos.repo.VoucherRepository;
import com.virgo.rekomendasos.repo.VoucherTransactionRepository;
import com.virgo.rekomendasos.service.AuthenticationService;
import com.virgo.rekomendasos.service.impl.VoucherServiceImpl;
import com.virgo.rekomendasos.utils.dto.VoucherDTO;
import com.virgo.rekomendasos.utils.specification.VoucherTransactionSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VoucherServiceTest {
    @Mock
    private VoucherRepository voucherRepository;
    @Mock
    private VoucherTransactionRepository voucherTransactionRepository;
    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private VoucherServiceImpl voucherService;

    private VoucherDTO validVoucherDto;
    private Voucher validVoucher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_Success() {
        // Data setup
        VoucherDTO newVoucherDto = new VoucherDTO();
        newVoucherDto.setName("New Voucher");
        newVoucherDto.setPrice(100);
        newVoucherDto.setQuantity(10);

        Voucher savedVoucher = new Voucher();
        savedVoucher.setId(1);
        savedVoucher.setName(newVoucherDto.getName());
        savedVoucher.setPrice(newVoucherDto.getPrice());
        savedVoucher.setQuantity(newVoucherDto.getQuantity());

        // Stubbing
        when(voucherRepository.save(any(Voucher.class))).thenReturn(savedVoucher);

        // Call the method under test
        Voucher result = voucherService.create(newVoucherDto);

        // Assertions
        assertNotNull(result, "The saved voucher should not be null");
        assertEquals(savedVoucher.getName(), result.getName());
        assertEquals(savedVoucher.getPrice(), result.getPrice());
        assertEquals(savedVoucher.getQuantity(), result.getQuantity());
        verify(voucherRepository, times(1)).save(any(Voucher.class));
    }


    @Test
    void create_InvalidPriceOrQuantity() {
        VoucherDTO invalidVoucherDto = new VoucherDTO();
        invalidVoucherDto.setName("Invalid Voucher");
        invalidVoucherDto.setPrice(-100); // Harga tidak valid
        invalidVoucherDto.setQuantity(-10); // Kuantitas tidak valid

        when(voucherRepository.save(any(Voucher.class))).thenReturn(new Voucher());

        Voucher createdVoucher = voucherService.create(invalidVoucherDto);

        assertNotNull(createdVoucher);
        assertEquals(0, createdVoucher.getPrice()); // Harga harus disetel ke 0
        assertEquals(0, createdVoucher.getQuantity()); // Kuantitas harus disetel ke 0
        verify(voucherRepository, times(1)).save(any(Voucher.class));
    }


    @Test
    void findAll_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Voucher> vouchers = List.of(validVoucher);
        Page<Voucher> voucherPage = new PageImpl<>(vouchers, pageable, vouchers.size());

        when(voucherRepository.findAll(pageable)).thenReturn(voucherPage);

        Page<Voucher> result = voucherService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(validVoucher.getName(), result.getContent().get(0).getName());
        verify(voucherRepository, times(1)).findAll(pageable);
    }


    @Test
    void findById_Success() {
        when(voucherRepository.findById(validVoucher.getId())).thenReturn(Optional.of(validVoucher));

        Voucher result = voucherService.findById(validVoucher.getId());

        assertNotNull(result);
        assertEquals(validVoucher.getName(), result.getName());
        verify(voucherRepository, times(1)).findById(validVoucher.getId());
    }

    @Test
    void findById_NotFound() {
        when(voucherRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> voucherService.findById(1));
        // Tidak perlu verify karena expect exception
    }


    @Test
    void updateById_Success() {
        // Data setup
        Voucher existingVoucher = new Voucher();
        existingVoucher.setId(1);
        existingVoucher.setName("Existing Voucher");
        existingVoucher.setPrice(100);
        existingVoucher.setQuantity(10);

        VoucherDTO updatedVoucherDto = new VoucherDTO();
        updatedVoucherDto.setName("Updated Voucher");
        updatedVoucherDto.setPrice(200);
        updatedVoucherDto.setQuantity(20);

        Voucher updatedVoucher = new Voucher();
        updatedVoucher.setId(1);
        updatedVoucher.setName(updatedVoucherDto.getName());
        updatedVoucher.setPrice(updatedVoucherDto.getPrice());
        updatedVoucher.setQuantity(updatedVoucherDto.getQuantity());

        // Stubbing
        when(voucherRepository.findById(existingVoucher.getId())).thenReturn(Optional.of(existingVoucher));
        when(voucherRepository.save(any(Voucher.class))).thenReturn(updatedVoucher);

        // Call the method under test
        Voucher result = voucherService.updateById(existingVoucher.getId(), updatedVoucherDto);

        // Assertions
        assertNotNull(result);
        assertEquals(updatedVoucherDto.getName(), result.getName());
        assertEquals(updatedVoucherDto.getPrice(), result.getPrice());
        assertEquals(updatedVoucherDto.getQuantity(), result.getQuantity());
        verify(voucherRepository, times(1)).findById(existingVoucher.getId());
        verify(voucherRepository, times(1)).save(any(Voucher.class));
    }



    @Test
    void updateById_NotFound() {
        VoucherDTO updateDto = new VoucherDTO("UpdatedName", 2000, 20);
        when(voucherRepository.findById(validVoucher.getId())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> voucherService.updateById(validVoucher.getId(), updateDto));
    }

    @Test
    void deleteById_Success() {
        when(voucherRepository.findById(validVoucher.getId())).thenReturn(Optional.of(validVoucher));
        doNothing().when(voucherRepository).deleteById(validVoucher.getId());

        voucherService.deleteById(validVoucher.getId());

        verify(voucherRepository, times(1)).deleteById(validVoucher.getId());
    }

    @Test
    void deleteById_NotFound() {
        when(voucherRepository.findById(validVoucher.getId())).thenReturn(Optional.empty());

        voucherService.deleteById(validVoucher.getId());

        verify(voucherRepository, times(0)).deleteById(validVoucher.getId());
    }
}
