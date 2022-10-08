package com.example.care.payment.service;

import com.example.care.payment.dto.KaKaoPayApproveDTO;
import com.example.care.payment.dto.KaKaoPayReadyDTO;
import com.example.care.payment.dto.MemberShipDTO;

public interface PaymentService {

    KaKaoPayReadyDTO payStart(MemberShipDTO memberShipDTO, String username);

    void payApprove(String orderId, String pgToken, Long userId, String username);
}
