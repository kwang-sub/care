package com.example.care.membership.repository.history;

import com.example.care.membership.domain.MembershipHistory;
import com.example.care.product.domain.ProductCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipHistoryRepository extends JpaRepository<MembershipHistory, Long>,
        MembershipHistoryRepositoryCustom {

}
