package com.example.care.membership.repository.membership;

import com.example.care.membership.domain.Grade;
import com.example.care.membership.domain.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long>, MembershipRepositoryCustom {

    Membership findByGrade(Grade grade);

}
