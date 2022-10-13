package com.example.care.payment.service;

import com.example.care.payment.dto.KaKaoPayReadyDTO;
import com.example.care.payment.dto.MemberShipDTO;

import java.time.LocalDate;

public interface PaymentService {

    KaKaoPayReadyDTO payStart(MemberShipDTO memberShipDTO, Long userId);

    void payApprove(String orderId, String pgToken, Long userId);

    void membershipCompleteSch(LocalDate now);
}
