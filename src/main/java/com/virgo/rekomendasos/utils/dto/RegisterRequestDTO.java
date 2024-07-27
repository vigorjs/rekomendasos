package com.virgo.rekomendasos.utils.dto;

import com.virgo.rekomendasos.model.enums.Gender;
import com.virgo.rekomendasos.model.enums.Role;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

    private String firstname;
    private String lastname;

//    @Email()
    private String email;

    private String password;

    //disabled role request, sudah dihandle di service
    @Nullable
    private Role role;
    @Nullable
    private Gender gender;
    @Nullable
    private String address;
    @Nullable
    private String mobileNumber;

    @Nullable
    private MultipartFile photo;

}
