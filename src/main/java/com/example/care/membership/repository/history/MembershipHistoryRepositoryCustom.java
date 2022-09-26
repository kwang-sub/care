package com.example.care.membership.repository.history;

import com.example.care.membership.domain.Grade;
import com.example.care.membership.domain.MembershipHistory;
import com.example.care.product.domain.ProductCode;

public interface MembershipHistoryRepositoryCustom {
    MembershipHistory findValidMembership(String username);
}
