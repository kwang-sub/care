package com.example.care.membership.repository;

import com.example.care.membership.domain.MembershipDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipDetailRepository extends JpaRepository<MembershipDetail, Long> {
}
