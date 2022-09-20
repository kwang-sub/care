package com.example.care.membership.repository.history;

import com.example.care.membership.domain.MembershipHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipHistoryRepository extends JpaRepository<MembershipHistory, Long>,
        MembershipHistoryRepositoryCustom {

}
