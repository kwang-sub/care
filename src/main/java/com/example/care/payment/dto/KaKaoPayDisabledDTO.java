package com.example.care.payment.dto;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class KaKaoPayDisabledDTO {

    private String cid;
    private String sid;
    private String status;
    private LocalDateTime created_at;
    private LocalDateTime last_approved_at;
    private LocalDateTime inactivated_at;
}
