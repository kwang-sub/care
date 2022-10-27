package com.example.care.payment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TidDTO {
    private String tid;
    private String orderId;
}
