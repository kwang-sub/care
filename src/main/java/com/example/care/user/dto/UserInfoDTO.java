package com.example.care.user.dto;

import com.example.care.membership.domain.Grade;
import com.example.care.membership.domain.MembershipStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class UserInfoDTO {

    private Long userId;
    private String username;
    private String nickname;
    private String email;
    private String provider;
    private LocalDateTime userRegDate;

    private Long membershipHistoryId;
    private Grade grade;
    private MembershipStatus membershipStatus;
    private LocalDateTime membershipRegDate;
    private LocalDate membershipEndDate;
    private Integer cleanUseNum;
    private Integer counselUseNum;
    private Integer transportUseNum;

    private Integer cleanMaxNum;
    private Integer counselMaxNum;
    private Integer transportMaxNum;
}
