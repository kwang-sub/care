package com.example.care.payment.dto;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class KaKaoPayReadyDTO {
    private String tid; //결제 고유 번호
    private String next_redirect_app_url;   //모바일 앱 결제 페이지 url
    private String next_redirect_mobile_url;    //모바일 웹 결제 페이지 url
    private String next_redirect_pc_url;    //pc 웹 결제 페이지 url
    private String android_app_scheme;  //안드로이드 앱스킴
    private String ios_app_scheme;  //ios 앱스킴
    private LocalDateTime created_at;   //결제 준비 요청 시간

    private String orderId;
}
