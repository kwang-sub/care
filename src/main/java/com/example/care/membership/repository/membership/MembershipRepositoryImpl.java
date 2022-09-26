package com.example.care.membership.repository.membership;

import com.example.care.membership.domain.Membership;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.care.membership.domain.QMembership.membership;
import static com.example.care.product.domain.QMembershipProduct.membershipProduct;
import static com.example.care.product.domain.QProduct.product;

@RequiredArgsConstructor
public class MembershipRepositoryImpl implements MembershipRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Membership> findMembershipList() {
        return queryFactory.selectFrom(membership)
                .distinct()
                .leftJoin(membership.membershipProductList, membershipProduct).fetchJoin()
                .leftJoin(membershipProduct.product, product).fetchJoin()
                .fetch();
    }
}
