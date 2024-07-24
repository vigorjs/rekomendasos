package com.virgo.rekomendasos.service;

import com.virgo.rekomendasos.model.meta.User;
import com.virgo.rekomendasos.utils.dto.RegisterRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    User create(RegisterRequestDTO req);
    Page<User> getAll(Pageable pageable, String name);
    User getById(Integer id);
    void delete(Integer id);
    User updateById(Integer id, RegisterRequestDTO req);
    //    User update (fitur)
    User update(RegisterRequestDTO req);

    void uploadImage(MultipartFile file);
}
