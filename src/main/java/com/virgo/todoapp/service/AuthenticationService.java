package com.virgo.todoapp.service;

import com.virgo.todoapp.entity.meta.User;
import com.virgo.todoapp.utils.dto.AuthenticationRequestDTO;
import com.virgo.todoapp.utils.dto.AuthenticationResponseDTO;
import com.virgo.todoapp.utils.dto.RegisterRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {

    public AuthenticationResponseDTO register(RegisterRequestDTO request);

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request);

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    public User getUserAuthenticated();
}
