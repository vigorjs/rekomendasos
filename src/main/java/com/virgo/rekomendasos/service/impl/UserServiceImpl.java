package com.virgo.rekomendasos.service.impl;

import com.virgo.rekomendasos.config.advisers.exception.NotFoundException;
import com.virgo.rekomendasos.model.enums.Role;
import com.virgo.rekomendasos.model.meta.LogTransaction;
import com.virgo.rekomendasos.model.meta.User;
import com.virgo.rekomendasos.repo.LogTransactionsRepository;
import com.virgo.rekomendasos.repo.UserRepository;
import com.virgo.rekomendasos.service.*;
import com.virgo.rekomendasos.utils.FileUploadUtil;
import com.virgo.rekomendasos.utils.dto.LogTransactionDto;
import com.virgo.rekomendasos.utils.dto.RegisterRequestDTO;
import com.virgo.rekomendasos.utils.dto.restClientDto.CloudinaryResponse;
import com.virgo.rekomendasos.utils.dto.restClientDto.MidtransRequestDTO;
import com.virgo.rekomendasos.utils.dto.restClientDto.MidtransResponseDTO;
import com.virgo.rekomendasos.utils.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final MidtransService midtransService;
    private final LogTransactionService logTransactionService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;
    private final CloudinaryService cloudinaryService;
    private final LogTransactionsRepository logTransactionsRepository;

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
    public void updateBalance(User user, Long gross_amount) {
        user.setPoint(user.getPoint() +gross_amount);
        userRepository.save(user);
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

    @Override
    @Transactional
    public MidtransResponseDTO userTopup(MidtransRequestDTO req) {

        User user = authenticationService.getUserAuthenticated();

        reqUserTopUpValidation(req);
        MidtransResponseDTO midtransResponse = midtransService.chargePayment(req);

        LogTransactionDto logTransaction = LogTransactionDto.builder()
                .userId(user.getId())
                .orderId(midtransResponse.getOrder_id())
                .grossAmount(Long.valueOf(midtransResponse.getGross_amount().split("\\.")[0]))
                .status(midtransResponse.getTransaction_status())
                .build();
        logTransactionService.create(logTransaction);

        return midtransResponse;
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

    private void reqUserTopUpValidation(MidtransRequestDTO req){
        if (req.getPayment_type() == null){
            req.setPayment_type("bank_transfer");
        }
        if (req.getCustom_expiry() == null) {
            req.setCustom_expiry(new MidtransRequestDTO.CustomExpiry());
            req.getCustom_expiry().setUnit("minute");
            req.getCustom_expiry().setExpiry_duration(60);
            req.getCustom_expiry().setOrder_time(getFormattedOrderTime());
        }

        req.getTransaction_details().setOrder_id(getOrderId());
    }

    private String getFormattedOrderTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z")
                .withZone(ZoneId.of("Asia/Jakarta"));
        return formatter.format(Instant.now());
    }

    private String getOrderId(){
        Optional<LogTransaction> lastTransaction = logTransactionsRepository.findTopByOrderByIdDesc();
        Long lastId = lastTransaction.map(LogTransaction::getId).orElse(0L);

        return "TOPUP-" + (lastId + 1);
    }
}
