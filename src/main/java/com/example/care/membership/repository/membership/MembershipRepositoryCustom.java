package com.example.care.membership.repository.membership;

import com.example.care.membership.domain.Membership;

import java.util.List;

public interface MembershipRepositoryCustom {

    List<Membership> findMembershipList();
}
