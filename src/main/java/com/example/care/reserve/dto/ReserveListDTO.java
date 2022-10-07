package com.example.care.reserve.dto;

import com.example.care.reserve.domain.ReserveStatus;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@Builder
@ToString
public class ReserveListDTO {

    private Long reserveId;
    private Long reserveUserId;
    private String title;
    private String address;
    private String detailAddress;
    private LocalDate reserveDate;
    private Integer reserveTime;
    private ReserveStatus reserveStatus;
    private boolean cancel;
}
