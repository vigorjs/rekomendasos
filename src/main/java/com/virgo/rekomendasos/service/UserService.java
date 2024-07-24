package com.virgo.rekomendasos.service;

import com.virgo.rekomendasos.model.meta.User;
import com.virgo.rekomendasos.utils.dto.AuthenticationRequestDTO;
import com.virgo.rekomendasos.utils.dto.AuthenticationResponseDTO;
import com.virgo.rekomendasos.utils.dto.RegisterRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface UserService {
    AuthenticationResponseDTO create(RegisterRequestDTO req);
    Page<User> getAll(Pageable pageable, String name);
    User getById(Integer id);
    void delete(Integer id);
    //    User update (fitur)
    User updateById(RegisterRequestDTO req);
}
