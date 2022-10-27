package com.example.care.membership.repository.history;

import com.example.care.membership.domain.MembershipHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MembershipHistoryRepository extends JpaRepository<MembershipHistory, Long>,
        MembershipHistoryRepositoryCustom {

    @Query("select m from MembershipHistory m where m.user.id = :userId and m.status = 'ORDER'")
    MembershipHistory findMembershipHistoryByUserId(@Param("userId") Long userId);
}
