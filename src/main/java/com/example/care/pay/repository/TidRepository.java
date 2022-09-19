package com.example.care.pay.repository;

import com.example.care.pay.domain.Tid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TidRepository extends JpaRepository<Tid,Long> {
    Tid findByOrderId(String orderId);
}
