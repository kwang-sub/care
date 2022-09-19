package com.example.care.membership.repository;

import com.example.care.membership.domain.Membership;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.care.membership.domain.QMembership.*;
import static com.example.care.membership.domain.QMembershipDetail.*;

@RequiredArgsConstructor
public class MembershipRepositoryImpl implements MembershipRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Membership> findMembershipAll() {
        List<Membership> memberships = queryFactory
                .selectFrom(membership)
                .distinct()
                .leftJoin(membership.membershipDetails, membershipDetail).fetchJoin()
                .fetch();
        return memberships;
    }
}
