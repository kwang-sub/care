package com.example.care.reserve.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReserveStatus {
    RESERVE("예약 대기중"), CANCEL("취소");

    private String name;

}
