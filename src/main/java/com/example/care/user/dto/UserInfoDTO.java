package com.example.care.user.dto;

import com.example.care.membership.domain.Grade;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@ToString
public class UserInfoDTO {

    private String username;
    private String email;
    private String provider;
    private LocalDateTime userRegDate;

    private Grade grade;
    private LocalDateTime membershipRegDate;
    private LocalDate membershipEndDate;
    private Integer cleanUseNum;
    private Integer counselUseNum;
    private Integer transportUseNum;

    private Integer cleanMaxNum;
    private Integer counselMaxNum;
    private Integer transportMaxNum;
}
