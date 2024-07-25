package com.virgo.rekomendasos.service.impl;

import com.virgo.rekomendasos.config.JwtService;
import com.virgo.rekomendasos.config.advisers.exception.NotFoundException;
import com.virgo.rekomendasos.model.enums.Role;
import com.virgo.rekomendasos.model.meta.User;
import com.virgo.rekomendasos.repo.UserRepository;
import com.virgo.rekomendasos.service.AuthenticationService;
import com.virgo.rekomendasos.service.CloudinaryService;
import com.virgo.rekomendasos.service.UserService;
import com.virgo.rekomendasos.utils.FileUploadUtil;
import com.virgo.rekomendasos.utils.dto.RegisterRequestDTO;
import com.virgo.rekomendasos.utils.dto.restClientDto.CloudinaryResponse;
import com.virgo.rekomendasos.utils.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final CloudinaryService cloudinaryService;

    @Override
    public User create(RegisterRequestDTO req) {
        User user = User.builder()
                .firstname(req.getFirstname())
                .lastname(req.getLastname())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .address(req.getAddress())
                .mobileNumber(req.getMobileNumber())
                .gender(req.getGender() != null ? req.getGender() : null)
                .role(req.getRole() != null ? req.getRole() : Role.USER)
                .build();
        return userRepository.save(user);
    }

    @Override
    public Page<User> getAll(Pageable pageable, String name) {
//        User currentUser = authenticationService.getUserAuthenticated();
        Specification<User> spec = UserSpecification.getSpecification(name);;
        return userRepository.findAll(spec, pageable);
    }

    @Override
    public User getById(Integer id) {
        return userRepository.findById(id).orElseThrow( () -> new NotFoundException("User not Found") );
    }

    @Override
    public void delete(Integer id) {
        if (userRepository.existsById(id)){
            userRepository.deleteById(id);
        }else {
            throw new NotFoundException("Category dengan ID " + id + "tidak ditemukan");
        }
    }

    @Override
    public User updateById(Integer id, RegisterRequestDTO req) {
        User user = getById(id);

        updateUserDetails(user, req);

        return userRepository.save(user);
    }

    @Override
    public User update(RegisterRequestDTO req) {

        User currentUser = authenticationService.getUserAuthenticated();
        User user = getById(currentUser.getId());

        updateUserDetails(user, req);

        return userRepository.save(user);
    }

    @Override
    public void uploadImage(MultipartFile file){
        User currentUser = authenticationService.getUserAuthenticated();
        User user = getById(currentUser.getId());

        FileUploadUtil.assertAllowed(file, FileUploadUtil.IMAGE_PATTERN);

        String fileName = FileUploadUtil.getFileName(file.getOriginalFilename());
        CloudinaryResponse response = cloudinaryService.uploadFile(file, fileName);

        user.setPhoto(response.getUrl());
        user.setCloudinaryImageId(response.getPublicId());
        userRepository.save(user);
    }

    private void updateUserDetails(User user, RegisterRequestDTO req) {
        if (req.getAddress() != null && !req.getAddress().isEmpty()) {
            user.setAddress(req.getAddress());
        }
        if (req.getRole() != null) {
            user.setRole(req.getRole());
        }
        if (req.getGender() != null) {
            user.setGender(req.getGender());
        }
        if (req.getLastname() != null && !req.getLastname().isEmpty()) {
            user.setLastname(req.getLastname());
        }
        if (req.getFirstname() != null && !req.getFirstname().isEmpty()) {
            user.setFirstname(req.getFirstname());
        }
        if (req.getEmail() != null && !req.getEmail().isEmpty()) {
            user.setEmail(req.getEmail());
        }
        if (req.getMobileNumber() != null && !req.getMobileNumber().isEmpty()) {
            user.setMobileNumber(req.getMobileNumber());
        }
        if (req.getPassword() != null && !req.getPassword().isEmpty()) {
            user.setPassword(req.getPassword());
        }
    }
}
