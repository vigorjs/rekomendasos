package com.virgo.rekomendasos.service;

import com.virgo.rekomendasos.model.meta.User;
import com.virgo.rekomendasos.model.meta.Voucher;
import com.virgo.rekomendasos.model.meta.VoucherTransaction;
import com.virgo.rekomendasos.repo.VoucherTransactionRepository;
import com.virgo.rekomendasos.service.impl.VoucherTransactionServiceImpl;
import com.virgo.rekomendasos.utils.dto.VoucherTransactionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_Success() {
        VoucherTransactionDTO newVoucherTransaction = new VoucherTransactionDTO(1, 1, 5);
        User authenticatedUser = User.builder()
                .id(1)
                .firstname("Jhon")
                .lastname("Doe")
                .email("jhon@mail.com")
                .password("encodedPassword")
                .build();
        Voucher voucher = new Voucher(1, "Test Voucher", 100, 10);
        VoucherTransaction savedTransaction = new VoucherTransaction(1, authenticatedUser, voucher, 5);

        when(authenticationService.getUserAuthenticated()).thenReturn(authenticatedUser);
        when(voucherService.findById(1)).thenReturn(voucher);
        when(voucherTransactionRepository.save(any(VoucherTransaction.class))).thenReturn(savedTransaction);

        VoucherTransaction result = voucherTransactionService.create(newVoucherTransaction);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(authenticatedUser, result.getUser());
        assertEquals(voucher, result.getVoucher());
        assertEquals(5, result.getVoucherQuantity());

        verify(authenticationService, times(1)).getUserAuthenticated();
        verify(voucherService, times(1)).findById(1);
        verify(voucherTransactionRepository, times(1)).save(any(VoucherTransaction.class));
    }

    @Test
    void testFindAll_Success() {
        Pageable pageable = mock(Pageable.class);
        Page<VoucherTransaction> expectedPage = new PageImpl<>(Arrays.asList(
                new VoucherTransaction(1, new User(), new Voucher(), 5),
                new VoucherTransaction(2, new User(), new Voucher(), 3)
        ));

        when(voucherTransactionRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<VoucherTransaction> result = voucherTransactionService.findAll(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());

        verify(voucherTransactionRepository, times(1)).findAll(pageable);
    }

    @Test
    void testFindById_Success() {
        Integer transactionId = 1;
        VoucherTransaction expectedTransaction = new VoucherTransaction(transactionId, new User(), new Voucher(), 5);

        when(voucherTransactionRepository.findById(transactionId)).thenReturn(Optional.of(expectedTransaction));

        VoucherTransaction result = voucherTransactionService.findById(transactionId);

        assertNotNull(result);
        assertEquals(transactionId, result.getId());

        verify(voucherTransactionRepository, times(1)).findById(transactionId);
    }

    @Test
    void testFindById_NotFound() {
        Integer transactionId = 1;

        when(voucherTransactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> voucherTransactionService.findById(transactionId));

        verify(voucherTransactionRepository, times(1)).findById(transactionId);
    }

    @Test
    void testUpdateById_Success() {
        Integer transactionId = 1;
        VoucherTransactionDTO updatedDTO = new VoucherTransactionDTO(2, 2, 10);
        User user = User.builder()
                .id(2)
                .firstname("Jon")
                .lastname("Den")
                .email("jon@mail.com")
                .password("encodedPassword")
                .build();
        Voucher voucher = new Voucher(2, "New Voucher", 200, 20);
        VoucherTransaction existingTransaction = new VoucherTransaction(transactionId, new User(), new Voucher(), 5);
        VoucherTransaction updatedTransaction = new VoucherTransaction(transactionId, user, voucher, 10);

        when(voucherTransactionRepository.findById(transactionId)).thenReturn(Optional.of(existingTransaction));
        when(userService.getById(2)).thenReturn(user);
        when(voucherService.findById(2)).thenReturn(voucher);
        when(voucherTransactionRepository.save(any(VoucherTransaction.class))).thenReturn(updatedTransaction);

        VoucherTransaction result = voucherTransactionService.updateById(transactionId, updatedDTO);

        assertNotNull(result);
        assertEquals(transactionId, result.getId());
        assertEquals(user, result.getUser());
        assertEquals(voucher, result.getVoucher());
        assertEquals(10, result.getVoucherQuantity());

        verify(voucherTransactionRepository, times(1)).findById(transactionId);
        verify(userService, times(1)).getById(2);
        verify(voucherService, times(1)).findById(2);
        verify(voucherTransactionRepository, times(1)).save(any(VoucherTransaction.class));
    }

    @Test
    void testUpdateById_NotFound() {
        Integer transactionId = 1;
        VoucherTransactionDTO updatedDTO = new VoucherTransactionDTO(2, 2, 10);

        when(voucherTransactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> voucherTransactionService.updateById(transactionId, updatedDTO));

        verify(voucherTransactionRepository, times(1)).findById(transactionId);
        verify(voucherTransactionRepository, never()).save(any(VoucherTransaction.class));
    }

    @Test
    void testDeleteById_Success() {
        Integer transactionId = 1;

        voucherTransactionService.deleteById(transactionId);

        verify(voucherTransactionRepository, times(1)).deleteById(transactionId);
    }

    @Test
    void testUse_Success() {
        Integer transactionId = 1;
        Integer quantity = 2;
        User user = User.builder()
                .id(1)
                .firstname("Jhon")
                .lastname("Doe")
                .email("jhon@mail.com")
                .password("encodedPassword")
                .build();
        Voucher voucher = new Voucher(1, "Test Voucher", 100, 10);
        VoucherTransaction existingTransaction = new VoucherTransaction(transactionId, user, voucher, 5);
        VoucherTransaction updatedTransaction = new VoucherTransaction(transactionId, user, voucher, 3);

        when(voucherTransactionRepository.findById(transactionId)).thenReturn(Optional.of(existingTransaction));
        when(voucherTransactionRepository.save(any(VoucherTransaction.class))).thenReturn(updatedTransaction);

        VoucherTransaction result = voucherTransactionService.use(transactionId, quantity);

        assertNotNull(result);
        assertEquals(transactionId, result.getId());
        assertEquals(3, result.getVoucherQuantity());

        verify(voucherTransactionRepository, times(1)).findById(transactionId);
        verify(voucherTransactionRepository, times(1)).save(any(VoucherTransaction.class));
    }

    @Test
    void testUse_NotEnoughQuantity() {
        Integer transactionId = 1;
        Integer quantity = 10;
        User user = User.builder()
                .id(1)
                .firstname("Jhon")
                .lastname("Doe")
                .email("jhon@mail.com")
                .password("encodedPassword")
                .build();
        Voucher voucher = new Voucher(1, "Test Voucher", 100, 10);
        VoucherTransaction existingTransaction = new VoucherTransaction(transactionId, user, voucher, 5);

        when(voucherTransactionRepository.findById(transactionId)).thenReturn(Optional.of(existingTransaction));

        assertThrows(IllegalArgumentException.class, () -> voucherTransactionService.use(transactionId, quantity));

        verify(voucherTransactionRepository, times(1)).findById(transactionId);
        verify(voucherTransactionRepository, never()).save(any(VoucherTransaction.class));
    }
}