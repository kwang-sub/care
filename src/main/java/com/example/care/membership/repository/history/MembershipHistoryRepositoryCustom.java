package com.example.care.membership.repository.history;

import com.example.care.membership.domain.MembershipHistory;

import java.time.LocalDate;
import java.util.List;

public interface MembershipHistoryRepositoryCustom {
    MembershipHistory findValidMembership(Long userId);

    List<MembershipHistory> findCompleteMembershipHistory(LocalDate now);
}
