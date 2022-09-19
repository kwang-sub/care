package com.example.care.pay.service;

import com.example.care.pay.dto.KaKaoPayApproveDTO;
import com.example.care.pay.dto.TidDTO;

public interface PayService {

    void saveTid(TidDTO tidDTO);

    String searchTid(String orderId);

    void completePayment(KaKaoPayApproveDTO kaKaoPayApproveDTO);
}
