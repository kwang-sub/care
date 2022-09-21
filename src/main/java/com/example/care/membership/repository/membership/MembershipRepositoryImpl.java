package com.example.care.membership.repository.membership;

import com.example.care.membership.domain.Membership;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MembershipRepositoryImpl implements MembershipRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Membership> findMembershipAll() {
        return null;
    }
}
