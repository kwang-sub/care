package com.example.care.membership.repository.history;

import com.example.care.membership.domain.MembershipHistory;
import com.example.care.membership.domain.MembershipStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.example.care.membership.domain.QMembership.membership;
import static com.example.care.membership.domain.QMembershipHistory.membershipHistory;
import static com.example.care.payment.domain.QPayment.payment;
import static com.example.care.user.domain.QUser.user;

@RequiredArgsConstructor
public class MembershipHistoryRepositoryImpl implements MembershipHistoryRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public MembershipHistory findValidMembership(Long userId) {
        return queryFactory.selectFrom(membershipHistory)
                .join(membershipHistory.user, user).fetchJoin()
                .join(membershipHistory.membership, membership).fetchJoin()
                .join(membershipHistory.payment, payment).fetchJoin()
                .where(membershipHistory.user.id.eq(userId)
                        .and(membershipHistory.status.eq(MembershipStatus.ORDER)))
                .fetchFirst();
    }

    @Override
    public List<MembershipHistory> findCompleteMembershipHistory(LocalDate now) {
        return queryFactory.selectFrom(membershipHistory)
                .join(membershipHistory.payment, payment).fetchJoin()
                .join(membershipHistory.user, user).fetchJoin()
                .where(membershipHistory.status.eq(MembershipStatus.ORDER),
                        membershipHistory.endDate.loe(now))
                .fetch();
    }
}
