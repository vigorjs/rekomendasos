package com.virgo.rekomendasos.service;


import com.virgo.rekomendasos.model.meta.Voucher;
import com.virgo.rekomendasos.model.meta.VoucherTransaction;
import com.virgo.rekomendasos.repo.VoucherTransactionRepository;
import com.virgo.rekomendasos.service.AuthenticationService;
import com.virgo.rekomendasos.service.UserService;
import com.virgo.rekomendasos.service.VoucherService;
import com.virgo.rekomendasos.service.impl.VoucherTransactionServiceImpl;
import com.virgo.rekomendasos.utils.dto.VoucherTransactionDTO;
import com.virgo.rekomendasos.utils.dto.VoucherTransactionConvert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VoucherTransactionServiceTest {
    @Mock
    private VoucherTransactionRepository voucherTransactionRepository;

    @Mock
    private VoucherService voucherService;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private VoucherTransactionServiceImpl voucherTransactionService;

    private VoucherTransactionDTO voucherTransactionDTO;
    private VoucherTransaction voucherTransaction;
    private Voucher voucher;

    @BeforeEach
    void setUp() {
        // Initialize common test data
        voucherTransactionDTO = VoucherTransactionDTO.builder()
                .voucherId(1)
                .voucherQuantity(500)
                .build();

        voucherTransaction = new VoucherTransaction();
        voucherTransaction.setVoucher(new Voucher());
        voucherTransaction.setVoucherQuantity(5);
        // Assume a default user or mock user if necessary
        voucherTransaction.setUser(null);

        voucher = new Voucher();
        voucher.setId(1);
        voucher.setName("Test Voucher");

        // Mock the behavior of the voucher service and other dependencies
        when(voucherService.findById(anyInt())).thenReturn(voucher);
        when(authenticationService.getUserAuthenticated()).thenReturn(null); // Mock user authentication as null
        when(voucherTransactionRepository.save(any(VoucherTransaction.class))).thenReturn(voucherTransaction);
        when(voucherTransactionRepository.findById(anyInt())).thenReturn(Optional.of(voucherTransaction));
        when(userService.getById(anyInt())).thenReturn(null); // Mock userService getById as null
    }

    @Test
    void create_Success() {
        // When
        VoucherTransaction createdVoucherTransaction = voucherTransactionService.create(voucherTransactionDTO);

        // Then
        assertNotNull(createdVoucherTransaction);
        assertEquals(1, createdVoucherTransaction.getVoucher().getId()); // Asumsikan ID harus 1
        assertEquals(5, createdVoucherTransaction.getVoucherQuantity());
    }

    @Test
    void findAll_Success() {
        // Given
        Pageable pageable = Pageable.unpaged();
        Page<VoucherTransaction> page = new PageImpl<>(Collections.singletonList(voucherTransaction));
        when(voucherTransactionRepository.findAll(pageable)).thenReturn(page);

        // When
        Page<VoucherTransaction> result = voucherTransactionService.findAll(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(voucherTransaction, result.getContent().get(0));
    }

    @Test
    void findById_Success() {
        // When
        VoucherTransaction foundVoucherTransaction = voucherTransactionService.findById(1);

        // Then
        assertNotNull(foundVoucherTransaction);
        assertEquals(voucherTransaction, foundVoucherTransaction);
    }

    @Test
    void findById_NotFound() {
        // Given
        when(voucherTransactionRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            voucherTransactionService.findById(1);
        });
        assertEquals("Voucher Not Found", thrown.getMessage());
    }

    @Test
    void updateById_Success() {
        // Given
        VoucherTransactionDTO updatedDTO = new VoucherTransactionDTO();
        updatedDTO.setVoucherId(1);
        updatedDTO.setVoucherQuantity(10);

        // When
        VoucherTransaction updatedVoucherTransaction = voucherTransactionService.updateById(1, updatedDTO);

        // Then
        assertNotNull(updatedVoucherTransaction);
        assertEquals(10, updatedVoucherTransaction.getVoucherQuantity());
    }

    @Test
    void updateById_NotFound() {
        // Given
        VoucherTransactionDTO updatedDTO = new VoucherTransactionDTO();
        updatedDTO.setVoucherId(1);
        updatedDTO.setVoucherQuantity(10);
        when(voucherTransactionRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            voucherTransactionService.updateById(1, updatedDTO);
        });
        assertEquals("Voucher Not Found", thrown.getMessage());
    }

    @Test
    void deleteById_Success() {
        // When
        voucherTransactionService.deleteById(1);

        // Then
        verify(voucherTransactionRepository, times(1)).deleteById(1);
    }

    @Test
    void use_Success() {
        // Given
        int quantityToUse = 3;
        voucherTransaction.setVoucherQuantity(5);

        // When
        VoucherTransaction updatedVoucherTransaction = voucherTransactionService.use(1, quantityToUse);

        // Then
        assertNotNull(updatedVoucherTransaction);
        assertEquals(2, updatedVoucherTransaction.getVoucherQuantity()); // 5 - 3 = 2
    }

    @Test
    void use_NotFound() {
        // Given
        int quantityToUse = 3;
        when(voucherTransactionRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            voucherTransactionService.use(1, quantityToUse);
        });
        assertEquals("Voucher Not Found", thrown.getMessage());
    }
}
