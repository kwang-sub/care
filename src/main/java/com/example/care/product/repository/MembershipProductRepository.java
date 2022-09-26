package com.example.care.product.repository;

import com.example.care.product.domain.MembershipProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipProductRepository extends JpaRepository<MembershipProduct, Long> {
}
