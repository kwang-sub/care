package com.example.care.payment.repository;

import com.example.care.payment.domain.Tid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TidRepository extends JpaRepository<Tid,Long> {
    Tid findByOrderId(String orderId);
}
