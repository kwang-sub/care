package com.example.care.reserve.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ReserveCancelDTO {

    private Long userId;
    private Long reserveId;
}
