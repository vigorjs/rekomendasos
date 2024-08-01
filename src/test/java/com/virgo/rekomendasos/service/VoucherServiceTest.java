package com.virgo.rekomendasos.service;

import com.virgo.rekomendasos.model.meta.User;
import com.virgo.rekomendasos.model.meta.Voucher;
import com.virgo.rekomendasos.model.meta.VoucherTransaction;
import com.virgo.rekomendasos.repo.VoucherRepository;
import com.virgo.rekomendasos.repo.VoucherTransactionRepository;
import com.virgo.rekomendasos.service.impl.VoucherServiceImpl;
import com.virgo.rekomendasos.utils.dto.VoucherDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VoucherServiceTest {

    @Mock
    private VoucherRepository voucherRepository;

    @Mock
    private VoucherTransactionRepository voucherTransactionRepository;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private VoucherServiceImpl voucherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_Success() {
        VoucherDTO newVoucher = new VoucherDTO("Test Voucher", 100, 10);
        Voucher savedVoucher = new Voucher(1, "Test Voucher", 100, 10);

        when(voucherRepository.save(any(Voucher.class))).thenReturn(savedVoucher);

        Voucher result = voucherService.create(newVoucher);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test Voucher", result.getName());
        assertEquals(100, result.getPrice());
        assertEquals(10, result.getQuantity());

        verify(voucherRepository, times(1)).save(any(Voucher.class));
    }

    @Test
    void testCreate_WithNegativeValues() {
        VoucherDTO newVoucher = new VoucherDTO("Test Voucher", -100, -10);
        Voucher savedVoucher = new Voucher(1, "Test Voucher", 0, 0);

        when(voucherRepository.save(any(Voucher.class))).thenReturn(savedVoucher);

        Voucher result = voucherService.create(newVoucher);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test Voucher", result.getName());
        assertEquals(0, result.getPrice());
        assertEquals(0, result.getQuantity());

        verify(voucherRepository, times(1)).save(any(Voucher.class));
    }

    @Test
    void testFindAll_Success() {
        Pageable pageable = mock(Pageable.class);
        Page<Voucher> expectedPage = new PageImpl<>(Arrays.asList(
                new Voucher(1, "Voucher 1", 100, 10),
                new Voucher(2, "Voucher 2", 200, 20)
        ));

        when(voucherRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<Voucher> result = voucherService.findAll(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Voucher 1", result.getContent().get(0).getName());
        assertEquals("Voucher 2", result.getContent().get(1).getName());

        verify(voucherRepository, times(1)).findAll(pageable);
    }

    @Test
    void testFindAllVoucherByUserId_Success() {
        Pageable pageable = mock(Pageable.class);
        User authenticatedUser = new User();
        VoucherTransaction vt1 = new VoucherTransaction(1, authenticatedUser, new Voucher(1, "Voucher 1", 100, 10), 1);
        VoucherTransaction vt2 = new VoucherTransaction(2, authenticatedUser, new Voucher(2, "Voucher 2", 200, 20), 2);
        Page<VoucherTransaction> voucherTransactions = new PageImpl<>(Arrays.asList(vt1, vt2));

        when(authenticationService.getUserAuthenticated()).thenReturn(authenticatedUser);
        when(voucherTransactionRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(voucherTransactions);

        Page<Voucher> result = voucherService.findAllVoucherByUserId(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Voucher 1", result.getContent().get(0).getName());
        assertEquals("Voucher 2", result.getContent().get(1).getName());

        verify(authenticationService, times(1)).getUserAuthenticated();
        verify(voucherTransactionRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void testFindById_Success() {
        Integer voucherId = 1;
        Voucher expectedVoucher = new Voucher(voucherId, "Test Voucher", 100, 10);

        when(voucherRepository.findById(voucherId)).thenReturn(Optional.of(expectedVoucher));

        Voucher result = voucherService.findById(voucherId);

        assertNotNull(result);
        assertEquals(voucherId, result.getId());
        assertEquals("Test Voucher", result.getName());

        verify(voucherRepository, times(1)).findById(voucherId);
    }

    @Test
    void testFindById_NotFound() {
        Integer voucherId = 1;

        when(voucherRepository.findById(voucherId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> voucherService.findById(voucherId));

        verify(voucherRepository, times(1)).findById(voucherId);
    }

    @Test
    void testFindByUserIdAndId_Success() {
        Integer voucherId = 1;
        User authenticatedUser = new User();
        Voucher expectedVoucher = new Voucher(voucherId, "Test Voucher", 100, 10);
        VoucherTransaction vt = new VoucherTransaction(1, authenticatedUser, expectedVoucher, 1);

        when(authenticationService.getUserAuthenticated()).thenReturn(authenticatedUser);
        when(voucherTransactionRepository.findAll(any(Specification.class))).thenReturn(List.of(vt));
        when(voucherRepository.findById(voucherId)).thenReturn(Optional.of(expectedVoucher));

        Voucher result = voucherService.findByUserIdAndId(voucherId);

        assertNotNull(result);
        assertEquals(voucherId, result.getId());
        assertEquals("Test Voucher", result.getName());

        verify(authenticationService, times(1)).getUserAuthenticated();
        verify(voucherTransactionRepository, times(1)).findAll(any(Specification.class));
        verify(voucherRepository, times(1)).findById(voucherId);
    }

    @Test
    void testFindByUserIdAndId_NotFound() {
        Integer voucherId = 1;
        User authenticatedUser = new User();

        when(authenticationService.getUserAuthenticated()).thenReturn(authenticatedUser);
        when(voucherTransactionRepository.findAll(any(Specification.class))).thenReturn(List.of());

        assertThrows(RuntimeException.class, () -> voucherService.findByUserIdAndId(voucherId));

        verify(authenticationService, times(1)).getUserAuthenticated();
        verify(voucherTransactionRepository, times(1)).findAll(any(Specification.class));
        verify(voucherRepository, never()).findById(any());
    }

    @Test
    void testUpdateById_Success() {
        Integer voucherId = 1;
        VoucherDTO updatedVoucher = new VoucherDTO("Updated Voucher", 200, 20);
        Voucher existingVoucher = new Voucher(voucherId, "Test Voucher", 100, 10);
        Voucher savedVoucher = new Voucher(voucherId, "Updated Voucher", 200, 20);

        when(voucherRepository.findById(voucherId)).thenReturn(Optional.of(existingVoucher));
        when(voucherRepository.save(any(Voucher.class))).thenReturn(savedVoucher);

        Voucher result = voucherService.updateById(voucherId, updatedVoucher);

        assertNotNull(result);
        assertEquals(voucherId, result.getId());
        assertEquals("Updated Voucher", result.getName());
        assertEquals(200, result.getPrice());
        assertEquals(20, result.getQuantity());

        verify(voucherRepository, times(1)).findById(voucherId);
        verify(voucherRepository, times(1)).save(any(Voucher.class));
    }

    @Test
    void testUpdateById_NotFound() {
        Integer voucherId = 1;
        VoucherDTO updatedVoucher = new VoucherDTO("Updated Voucher", 200, 20);

        when(voucherRepository.findById(voucherId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> voucherService.updateById(voucherId, updatedVoucher));

        verify(voucherRepository, times(1)).findById(voucherId);
        verify(voucherRepository, never()).save(any(Voucher.class));
    }

    @Test
    void testDeleteById_Success() {
        Integer voucherId = 1;

        voucherService.deleteById(voucherId);

        verify(voucherRepository, times(1)).deleteById(voucherId);
    }
}