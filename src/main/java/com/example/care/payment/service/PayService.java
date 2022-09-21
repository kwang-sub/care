package com.example.care.payment.service;

import com.example.care.payment.dto.KaKaoPayApproveDTO;
import com.example.care.payment.dto.TidDTO;

public interface PayService {

    void saveTid(TidDTO tidDTO);

    String findTid(String orderId);

    String completePayment(KaKaoPayApproveDTO kaKaoPayApproveDTO);

}
