package com.virgo.todoapp.utils.dto;

import com.virgo.todoapp.entity.enums.Gender;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
//    private Role role;
    @Nullable
    private Gender gender;
    @Nullable
    private String address;
    @Nullable
    private String mobileNumber;

}
