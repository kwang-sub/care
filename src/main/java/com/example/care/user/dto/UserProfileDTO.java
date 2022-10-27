package com.example.care.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
public class UserProfileDTO {
    private Long userId;
    private String username;
    @NotBlank
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$")
    private String nickname;
}