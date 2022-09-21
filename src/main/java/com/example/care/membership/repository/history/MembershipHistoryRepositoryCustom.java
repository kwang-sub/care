package com.example.care.membership.repository.history;

import com.example.care.membership.domain.Grade;
import com.example.care.membership.domain.MembershipHistory;

public interface MembershipHistoryRepositoryCustom {
    MembershipHistory findValidMembership(String username);
}
