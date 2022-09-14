package com.example.care.user.dto;

import com.example.care.user.domain.Role;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
public class UserJoinDTO {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z]{1}[a-zA-Z0-9]{4,11}$" )
    private String username;
    @NotBlank
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$")
    private String nickname;
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$")
    private String password;
    @NotBlank
    private String checkPassword;
    @NotBlank
    @Email(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$")
    private String email;
    private Role role;
    private String provider;
    private String providerId;

    public String getRoleKey() {
        return this.role.getKey();
    }
}
