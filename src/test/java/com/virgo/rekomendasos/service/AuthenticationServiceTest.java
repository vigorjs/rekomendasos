package com.virgo.rekomendasos.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virgo.rekomendasos.config.JwtService;
import com.virgo.rekomendasos.config.advisers.exception.NotFoundException;
import com.virgo.rekomendasos.model.enums.Role;
import com.virgo.rekomendasos.model.enums.TokenType;
import com.virgo.rekomendasos.model.meta.Token;
import com.virgo.rekomendasos.model.meta.User;
import com.virgo.rekomendasos.repo.TokenRepository;
import com.virgo.rekomendasos.repo.UserRepository;
import com.virgo.rekomendasos.service.impl.AuthenticationServiceImpl;
import com.virgo.rekomendasos.utils.dto.AuthenticationRequestDTO;
import com.virgo.rekomendasos.utils.dto.AuthenticationResponseDTO;
import com.virgo.rekomendasos.utils.dto.RegisterRequestDTO;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private User user;
    private RegisterRequestDTO registerRequest;
    private AuthenticationRequestDTO authenticationRequest;
    private Token token;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .photo("https://ui.shadcn.com/avatars/03.png")
                .build();

        registerRequest = RegisterRequestDTO.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("password")
                .build();

        authenticationRequest = AuthenticationRequestDTO.builder()
                .email("john.doe@example.com")
                .password("password")
                .build();

        token = Token.builder()
                .user(user)
                .token("sample-jwt-token")
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void register_Success() {
        // Given
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("sample-jwt-token");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("sample-refresh-token");

        // When
        AuthenticationResponseDTO response = authenticationService.register(registerRequest);

        // Then
        assertNotNull(response);
        assertEquals("sample-jwt-token", response.getAccessToken());
        assertEquals("sample-refresh-token", response.getRefreshToken());
        verify(userRepository).save(any(User.class));
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void authenticate_Success() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("sample-jwt-token");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("sample-refresh-token");

        // When
        AuthenticationResponseDTO response = authenticationService.authenticate(authenticationRequest);

        // Then
        assertNotNull(response);
        assertEquals("sample-jwt-token", response.getAccessToken());
        assertEquals("sample-refresh-token", response.getRefreshToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void authenticate_UserNotFound() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> authenticationService.authenticate(authenticationRequest));
    }

    @Test
    void refreshToken_Success() throws IOException {
        // Given
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer sample-refresh-token");
        when(jwtService.extractUsername("sample-refresh-token")).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid("sample-refresh-token", user)).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("new-access-token");

        // Mocking ObjectMapper behavior
        OutputStream outputStream = mock(OutputStream.class);
        when(response.getOutputStream()).thenReturn(new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }
            @Override
            public boolean isReady() {
                return true;
            }
            @Override
            public void setWriteListener(WriteListener writeListener) {
            }
        });

        // When
        authenticationService.refreshToken(request, response);

        // Then
        verify(response).getOutputStream();
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void refreshToken_Failure() throws IOException {
        // Given
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer invalid-refresh-token");
        when(jwtService.extractUsername("invalid-refresh-token")).thenReturn(null);

        // When
        authenticationService.refreshToken(request, response);

        // Then
        verify(response, never()).getOutputStream();
        verify(tokenRepository, never()).save(any(Token.class));
    }

    @Test
    void getUserAuthenticated_Success() {
        // Given
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // When
        User authenticatedUser = authenticationService.getUserAuthenticated();

        // Then
        assertNotNull(authenticatedUser);
        assertEquals(user.getEmail(), authenticatedUser.getEmail());
    }

    @Test
    void getUserAuthenticated_NotFound() {
        // Given
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("unknown@example.com");
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> authenticationService.getUserAuthenticated());
    }
}
