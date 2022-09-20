package com.example.care.pay.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
public class TidDTO {
    private String tid;
    private String orderId;
}
