package com.example.care.user.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserPwDTO {

    private Long userId;
    @NotBlank
    private String currentPw;
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$")
    private String modPw;
    @NotBlank
    private String checkModPw;
}
