package com.example.care.user.repository;

import com.example.care.membership.domain.MembershipStatus;
import com.example.care.membership.domain.QMembership;
import com.example.care.membership.domain.QMembershipHistory;
import com.example.care.product.domain.QMembershipProduct;
import com.example.care.product.domain.QProduct;
import com.example.care.user.domain.QUser;
import com.example.care.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.example.care.membership.domain.QMembership.membership;
import static com.example.care.membership.domain.QMembershipHistory.membershipHistory;
import static com.example.care.product.domain.QMembershipProduct.membershipProduct;
import static com.example.care.product.domain.QProduct.product;
import static com.example.care.user.domain.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @Override
    public User findUserInfoByUserId(Long userId) {
        return queryFactory.selectFrom(user)
                .leftJoin(user.membershipHistoryList, membershipHistory).fetchJoin()
                .leftJoin(membershipHistory.membership, membership).fetchJoin()
                .where(user.id.eq(userId))
                .orderBy(membershipHistory.id.desc())
                .fetchOne();
    }
}
