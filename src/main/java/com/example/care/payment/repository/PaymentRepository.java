package com.example.care.payment.repository;

import com.example.care.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

    Payment findBySid(String sid);
}
