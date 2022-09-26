package com.example.care.payment.dto;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class KaKaoPayDisabledDTO {

    private String cid; //가맹점 코드
    private String sid; //정기결제 고유번호
    private String status;  //정기결제상태
    private LocalDateTime created_at;   //sid 발급 시각
    private LocalDateTime last_approved_at; //마지막 결제승인 시각
    private LocalDateTime inactivated_at;   //정기결제 비활성화 시각
}
