package com.example.care.membership.repository;

import com.example.care.membership.domain.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long>, MembershipRepositoryCustom {

}
