package com.example.care.membership.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MembershipStatus {
    ORDER("이용"), CANCEL("해지"), COMPLETE("만료");

    private String name;
}
