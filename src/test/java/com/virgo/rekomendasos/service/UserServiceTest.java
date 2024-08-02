package com.virgo.rekomendasos.service;

import com.virgo.rekomendasos.config.advisers.exception.NotFoundException;
import com.virgo.rekomendasos.model.enums.Gender;
import com.virgo.rekomendasos.model.enums.Role;
import com.virgo.rekomendasos.model.meta.LogTransaction;
import com.virgo.rekomendasos.model.meta.User;
import com.virgo.rekomendasos.repo.LogTransactionsRepository;
import com.virgo.rekomendasos.repo.UserRepository;
import com.virgo.rekomendasos.service.AuthenticationService;
import com.virgo.rekomendasos.service.CloudinaryService;
import com.virgo.rekomendasos.service.LogTransactionService;
import com.virgo.rekomendasos.service.MidtransService;
import com.virgo.rekomendasos.service.impl.UserServiceImpl;
import com.virgo.rekomendasos.utils.dto.RegisterRequestDTO;
import com.virgo.rekomendasos.utils.dto.restClientDto.CloudinaryResponse;
import com.virgo.rekomendasos.utils.dto.restClientDto.MidtransRequestDTO;
import com.virgo.rekomendasos.utils.dto.restClientDto.MidtransResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private CloudinaryService cloudinaryService;
    @Mock
    private MidtransService midtransService;
    @Mock
    private LogTransactionService logTransactionService;
    @Mock
    private LogTransactionsRepository logTransactionsRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSuccess() {
        RegisterRequestDTO req = new RegisterRequestDTO();
        req.setFirstname("John");
        req.setLastname("Doe");
        req.setEmail("john@example.com");
        req.setPassword("password");
        req.setRole(Role.USER);

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User result = userService.create(req);

        assertNotNull(result);
        assertEquals("John", result.getFirstname());
        assertEquals("Doe", result.getLastname());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(Role.USER, result.getRole());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testCreateWithNullFields() {
        RegisterRequestDTO req = new RegisterRequestDTO();
        req.setFirstname("John");
        req.setEmail("john@example.com");
        req.setPassword("password");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User result = userService.create(req);

        assertNotNull(result);
        assertEquals("John", result.getFirstname());
        assertNull(result.getLastname());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(Role.USER, result.getRole());  // Default role

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testGetAllSuccess() {
        Pageable pageable = mock(Pageable.class);
        Page<User> mockPage = new PageImpl<>(Collections.singletonList(new User()));

        when(userRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(mockPage);

        Page<User> result = userService.getAll(pageable, "John");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(userRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void testGetAllEmptyResult() {
        Pageable pageable = mock(Pageable.class);
        Page<User> mockPage = new PageImpl<>(Collections.emptyList());

        when(userRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(mockPage);

        Page<User> result = userService.getAll(pageable, "NonExistentName");

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());

        verify(userRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void testGetByIdSuccess() {
        User mockUser = new User();
        mockUser.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));

        User result = userService.getById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());

        verify(userRepository).findById(1);
    }

    @Test
    void testGetByIdNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getById(1));

        verify(userRepository).findById(1);
    }

    @Test
    void testDeleteSuccess() {
        when(userRepository.existsById(1)).thenReturn(true);

        userService.delete(1);

        verify(userRepository).deleteById(1);
    }

    @Test
    void testDeleteNotFound() {
        when(userRepository.existsById(1)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.delete(1));

        verify(userRepository, never()).deleteById(anyInt());
    }

    @Test
    void testUpdateSuccess() {
        User currentUser = new User();
        currentUser.setId(1);

        RegisterRequestDTO req = new RegisterRequestDTO();
        req.setFirstname("Jane");
        req.setLastname("Doe");
        req.setEmail("jane@example.com");

        when(authenticationService.getUserAuthenticated()).thenReturn(currentUser);
        when(userRepository.findById(1)).thenReturn(Optional.of(currentUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User result = userService.update(req);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstname());
        assertEquals("Doe", result.getLastname());
        assertEquals("jane@example.com", result.getEmail());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdatePartialFields() {
        User currentUser = new User();
        currentUser.setId(1);
        currentUser.setFirstname("John");
        currentUser.setLastname("Doe");
        currentUser.setEmail("john@example.com");

        RegisterRequestDTO req = new RegisterRequestDTO();
        req.setFirstname("Jane");

        when(authenticationService.getUserAuthenticated()).thenReturn(currentUser);
        when(userRepository.findById(1)).thenReturn(Optional.of(currentUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User result = userService.update(req);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstname());
        assertEquals("Doe", result.getLastname());
        assertEquals("john@example.com", result.getEmail());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUploadImageSuccess() throws IOException {
        User currentUser = new User();
        currentUser.setId(1);

        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("test.jpg");
        when(mockFile.getContentType()).thenReturn("image/jpeg");

        CloudinaryResponse mockResponse = new CloudinaryResponse();
        mockResponse.setUrl("http://example.com/image.jpg");
        mockResponse.setPublicId("public_id");

        when(authenticationService.getUserAuthenticated()).thenReturn(currentUser);
        when(userRepository.findById(1)).thenReturn(Optional.of(currentUser));
        when(cloudinaryService.uploadFile(any(), anyString())).thenReturn(mockResponse);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        userService.uploadImage(mockFile);

        assertEquals("http://example.com/image.jpg", currentUser.getPhoto());
        assertEquals("public_id", currentUser.getCloudinaryImageId());

        verify(userRepository).save(currentUser);
    }

    @Test
    void testUploadImageInvalidFileType() {
        User currentUser = new User();
        currentUser.setId(1);

        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("test.txt");
        when(mockFile.getContentType()).thenReturn("text/plain");

        when(authenticationService.getUserAuthenticated()).thenReturn(currentUser);
        when(userRepository.findById(1)).thenReturn(Optional.of(currentUser));

        assertThrows(IllegalArgumentException.class, () -> userService.uploadImage(mockFile));

        verify(cloudinaryService, never()).uploadFile(any(), anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUserTopupSuccess() {
        User currentUser = new User();
        currentUser.setId(1);

        MidtransRequestDTO req = new MidtransRequestDTO();
        req.setPayment_type("bank_transfer");

        // Initialize TransactionDetails
        MidtransRequestDTO.TransactionDetails transactionDetails = new MidtransRequestDTO.TransactionDetails("order123", 10000L);
//        transactionDetails.setGross_amount(100000L);
        req.setTransaction_details(transactionDetails);

        MidtransResponseDTO mockResponse = new MidtransResponseDTO();
        mockResponse.setOrder_id("TOPUP-1");
        mockResponse.setGross_amount("100000.00");
        mockResponse.setTransaction_status("pending");

        when(authenticationService.getUserAuthenticated()).thenReturn(currentUser);
        when(midtransService.chargePayment(any())).thenReturn(mockResponse);
        when(logTransactionsRepository.findTopByOrderByIdDesc()).thenReturn(Optional.empty());

        MidtransResponseDTO result = userService.userTopup(req);

        assertNotNull(result);
        assertEquals("TOPUP-1", result.getOrder_id());
        assertEquals("100000.00", result.getGross_amount());
        assertEquals("pending", result.getTransaction_status());

        verify(logTransactionService).create(any());
        verify(logTransactionsRepository).findTopByOrderByIdDesc();
    }

    @Test
    void testUserTopupWithNullPaymentType() {
        User currentUser = new User();
        currentUser.setId(1);

        MidtransRequestDTO req = new MidtransRequestDTO();

        // Initialize TransactionDetails
        MidtransRequestDTO.TransactionDetails transactionDetails = new MidtransRequestDTO.TransactionDetails("order123", 100000L);
//        MidtransRequestDTO.TransactionDetails transactionDetails = new MidtransRequestDTO.TransactionDetails();
        transactionDetails.setGross_amount(100000L);
        req.setTransaction_details(transactionDetails);

        MidtransResponseDTO mockResponse = new MidtransResponseDTO();
        mockResponse.setOrder_id("TOPUP-1");
        mockResponse.setGross_amount("100000.00");
        mockResponse.setTransaction_status("pending");

        when(authenticationService.getUserAuthenticated()).thenReturn(currentUser);
        when(midtransService.chargePayment(any())).thenReturn(mockResponse);
        when(logTransactionsRepository.findTopByOrderByIdDesc()).thenReturn(Optional.empty());

        MidtransResponseDTO result = userService.userTopup(req);

        assertNotNull(result);
        assertEquals("TOPUP-1", result.getOrder_id());
        assertEquals("100000.00", result.getGross_amount());
        assertEquals("pending", result.getTransaction_status());
        assertEquals("bank_transfer", req.getPayment_type());

        verify(logTransactionService).create(any());
        verify(logTransactionsRepository).findTopByOrderByIdDesc();
    }

    @Test
    void testUserTopupWithExistingTransactions() {
        User currentUser = new User();
        currentUser.setId(1);

        MidtransRequestDTO req = new MidtransRequestDTO();
        req.setPayment_type("bank_transfer");

        // Initialize TransactionDetails

        MidtransRequestDTO.TransactionDetails transactionDetails = new MidtransRequestDTO.TransactionDetails("order123", 100000L);

//        MidtransRequestDTO.TransactionDetails transactionDetails = new MidtransRequestDTO.TransactionDetails();
        transactionDetails.setGross_amount(100000L);
        req.setTransaction_details(transactionDetails);

        MidtransResponseDTO mockResponse = new MidtransResponseDTO();
        mockResponse.setOrder_id("TOPUP-2");
        mockResponse.setGross_amount("100000.00");
        mockResponse.setTransaction_status("pending");

        when(authenticationService.getUserAuthenticated()).thenReturn(currentUser);
        when(midtransService.chargePayment(any())).thenReturn(mockResponse);

        LogTransaction lastTransaction = new LogTransaction();
        lastTransaction.setId(1L);
        when(logTransactionsRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(lastTransaction));

        MidtransResponseDTO result = userService.userTopup(req);

        assertNotNull(result);
        assertEquals("TOPUP-2", result.getOrder_id());
        assertEquals("100000.00", result.getGross_amount());
        assertEquals("pending", result.getTransaction_status());

        verify(logTransactionService).create(any());
        verify(logTransactionsRepository).findTopByOrderByIdDesc();
    }

    @Test
    void testUserTopupWithNullTransactionDetails() {
        User currentUser = new User();
        currentUser.setId(1);

        MidtransRequestDTO req = new MidtransRequestDTO();
        req.setPayment_type("bank_transfer");
        // Intentionally leave transaction_details as null

        when(authenticationService.getUserAuthenticated()).thenReturn(currentUser);

        assertThrows(NullPointerException.class, () -> userService.userTopup(req));
    }
}
