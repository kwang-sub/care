package com.example.care.payment.service;

import com.example.care.payment.dto.KaKaoPayApproveDTO;
import com.example.care.payment.dto.TidDTO;

public interface PaymentService {

    void saveTid(TidDTO tidDTO);

    String findTid(String orderId);

    String createPayment(KaKaoPayApproveDTO kaKaoPayApproveDTO, Long userId);

}
