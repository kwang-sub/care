package com.example.care.membership.repository.history;

import com.example.care.membership.domain.MembershipHistory;
import com.example.care.product.domain.ProductCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface MembershipHistoryRepository extends JpaRepository<MembershipHistory, Long>,
        MembershipHistoryRepositoryCustom {

    @Query("select m from MembershipHistory m where m.user.id = :userId and m.endDate >= :now")
    MembershipHistory findMembershipHistoryByUserId(@Param("userId") Long userId, @Param("now")LocalDate now);
}
