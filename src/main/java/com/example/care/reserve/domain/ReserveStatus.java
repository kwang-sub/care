package com.example.care.reserve.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReserveStatus {
    RESERVE("예약이용 대기중"), CANCEL("예약 취소"), COMPLETE("예약이용 완료");

    private String name;

}
