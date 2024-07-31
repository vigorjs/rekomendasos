package com.virgo.rekomendasos.service;

import com.virgo.rekomendasos.model.enums.TransactionStatus;
import com.virgo.rekomendasos.model.meta.LogTransaction;
import com.virgo.rekomendasos.model.meta.User;
import com.virgo.rekomendasos.repo.LogTransactionsRepository;
import com.virgo.rekomendasos.repo.UserRepository;
import com.virgo.rekomendasos.service.AuthenticationService;
import com.virgo.rekomendasos.service.MidtransService;
import com.virgo.rekomendasos.service.UserService;
import com.virgo.rekomendasos.service.impl.LogTransactionServiceImpl;
import com.virgo.rekomendasos.utils.dto.LogTransactionDto;
import com.virgo.rekomendasos.utils.dto.restClientDto.MidtransResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogTransactionServiceTest {

    @Mock
    private LogTransactionsRepository logTransactionsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private MidtransService midtransService;

    @Mock
    private UserService userService;

    @Mock
    private ExecutorService executorService;

    @InjectMocks
    private LogTransactionServiceImpl logTransactionService;

    private User user;
    private LogTransaction logTransaction;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .email("test@example.com")
                .firstname("John")
                .lastname("Doe")
                .password("password")
                .build();

        logTransaction = LogTransaction.builder()
                .id(1L)
                .order_id("order123")
                .gross_amount(1000L)
                .user(user)
                .status(TransactionStatus.pending)
                .build();
    }

    @Test
    void create_Success() {
        LogTransactionDto logTransactionDto = new LogTransactionDto();
        logTransactionDto.setOrderId("order123");
        logTransactionDto.setGrossAmount(1000L);
        logTransactionDto.setUserId(1);
        logTransactionDto.setStatus("pending");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(logTransactionsRepository.save(any(LogTransaction.class))).thenReturn(logTransaction);

        logTransactionService.create(logTransactionDto);

        verify(executorService, times(1)).submit(any(Runnable.class));
    }

    @Test
    void create_UserNotFound() {
        LogTransactionDto logTransactionDto = new LogTransactionDto();
        logTransactionDto.setUserId(1);

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> logTransactionService.create(logTransactionDto));
        assertEquals("User not found", thrown.getMessage());
    }

    @Test
    void findById_Success() {
        when(logTransactionsRepository.findById(1L)).thenReturn(Optional.of(logTransaction));

        LogTransaction result = logTransactionService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void findById_NotFound() {
        when(logTransactionsRepository.findById(1L)).thenReturn(Optional.empty());

        LogTransaction result = logTransactionService.findById(1L);

        assertNull(result);
    }

    @Test
    void update_Success() {
        LogTransactionDto logTransactionDto = new LogTransactionDto();
        logTransactionDto.setGrossAmount(2000L);
        logTransactionDto.setStatus("settlement");
        logTransactionDto.setUserId(1);

        when(logTransactionsRepository.findById(1L)).thenReturn(Optional.of(logTransaction));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(logTransactionsRepository.save(any(LogTransaction.class))).thenReturn(logTransaction);

        LogTransaction result = logTransactionService.update(1L, logTransactionDto);

        assertNotNull(result);
        assertEquals(2000L, result.getGross_amount());
        assertEquals(TransactionStatus.settlement, result.getStatus());
    }

//    @Test
//    void update_NotFound() {
//        LogTransactionDto logTransactionDto = new LogTransactionDto();
//        logTransactionDto.setUserId(1);
//
//        when(logTransactionsRepository.findById(1L)).thenReturn(Optional.empty());
//
//        LogTransaction result = logTransactionService.update(1L, logTransactionDto);
//
//        assertNull(result);
//    }

    @Test
    void update_NotFound() {
        // Arrange
        LogTransactionDto dto = new LogTransactionDto("order12", 1000L, "pending", 1);
        when(logTransactionsRepository.findById(anyLong())).thenReturn(Optional.empty()); // Mock transaction not found
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty()); // Mock user not found

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            logTransactionService.update(1L, dto);
        });

        // Assert the exception message
        assertEquals("User not found", thrown.getMessage());
    }

    @Test
    void deleteById_Success() {
        when(logTransactionsRepository.findById(1L)).thenReturn(Optional.of(logTransaction));

        logTransactionService.deleteById(1L);

        verify(logTransactionsRepository, times(1)).delete(logTransaction);
    }

    @Test
    void deleteById_NotFound() {
        when(logTransactionsRepository.findById(1L)).thenReturn(Optional.empty());

        logTransactionService.deleteById(1L);

        verify(logTransactionsRepository, never()).delete(any(LogTransaction.class));
    }

    @Test
    void findAllUserTransactions_Success() {
        when(authenticationService.getUserAuthenticated()).thenReturn(user);
        when(logTransactionsRepository.findAll()).thenReturn(List.of(logTransaction));

        List<LogTransaction> result = logTransactionService.findAllUserTransactions();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void findAllUserTransactions_UserNotAuthenticated() {
        when(authenticationService.getUserAuthenticated()).thenReturn(null);

        List<LogTransaction> result = logTransactionService.findAllUserTransactions();

        assertNull(result);
    }

    @Test
    void updateLogTransactionStatus_Success() {
        LogTransactionDto logTransactionDto = new LogTransactionDto();
        logTransactionDto.setOrderId("order123");

        MidtransResponseDTO responseDTO = new MidtransResponseDTO();
        responseDTO.setTransaction_status(String.valueOf(TransactionStatus.settlement));

        when(midtransService.getStatus("order123")).thenReturn(responseDTO);
        when(logTransactionsRepository.findById(1L)).thenReturn(Optional.of(logTransaction));
        doNothing().when(userService).updateBalance(any(User.class), anyLong());

        logTransactionService.updateLogTransactionStatus(1L, logTransactionDto);

        verify(midtransService, never()).changeStatus(anyString(), anyString());
        verify(userService).updateBalance(any(User.class), anyLong());
    }

    @Test
    void updateLogTransactionStatus_Failure() {
        LogTransactionDto logTransactionDto = new LogTransactionDto();
        logTransactionDto.setOrderId("order123");

        MidtransResponseDTO responseDTO = new MidtransResponseDTO();
        responseDTO.setTransaction_status(String.valueOf(TransactionStatus.failure));

        when(midtransService.getStatus("order123")).thenReturn(responseDTO);

        logTransactionService.updateLogTransactionStatus(1L, logTransactionDto);

        verify(midtransService).changeStatus("order123", String.valueOf(TransactionStatus.failure));
    }
}
