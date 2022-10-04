package com.example.care.product.repository;

import com.example.care.product.domain.MembershipProduct;
import com.example.care.product.domain.ProductCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MembershipProductRepository extends JpaRepository<MembershipProduct, Long> {
    @Query("select m.maxNum from MembershipProduct m  where m.product.code = :code and m.membership.id = :membershipId")
    int findMaxNumByProductCode(@Param("code") ProductCode code, @Param("membershipId") Long membershipId);
}
