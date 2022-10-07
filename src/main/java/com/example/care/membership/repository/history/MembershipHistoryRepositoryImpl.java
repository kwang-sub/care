package com.example.care.membership.repository.history;

import com.example.care.membership.domain.MembershipHistory;
import com.example.care.membership.domain.MembershipStatus;
import com.example.care.payment.domain.QPayment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.example.care.membership.domain.QMembership.membership;
import static com.example.care.membership.domain.QMembershipHistory.membershipHistory;
import static com.example.care.payment.domain.QPayment.payment;
import static com.example.care.user.domain.QUser.user;

@RequiredArgsConstructor
public class MembershipHistoryRepositoryImpl implements MembershipHistoryRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public MembershipHistory findValidMembership(String username) {
        return queryFactory.selectFrom(membershipHistory)
                .join(membershipHistory.user, user).fetchJoin()
                .join(membershipHistory.membership, membership).fetchJoin()
                .join(membershipHistory.payment, payment).fetchJoin()
                .where(membershipHistory.user.username.eq(username)
                        .and(membershipHistory.status.eq(MembershipStatus.ORDER)))
                .fetchFirst();
    }
}
