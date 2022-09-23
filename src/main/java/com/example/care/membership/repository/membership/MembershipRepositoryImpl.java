package com.example.care.membership.repository.membership;

import com.example.care.membership.domain.Membership;
import com.example.care.membership.domain.QMembership;
import com.example.care.product.domain.QProduct;
import com.example.care.product.domain.QProductMembership;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.care.membership.domain.QMembership.membership;
import static com.example.care.product.domain.QProduct.product;
import static com.example.care.product.domain.QProductMembership.*;

@RequiredArgsConstructor
public class MembershipRepositoryImpl implements MembershipRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Membership> findMembershipList() {
        return queryFactory.selectFrom(membership)
                .distinct()
                .leftJoin(membership.productMembershipList, productMembership).fetchJoin()
                .leftJoin(productMembership.product, product).fetchJoin()
                .fetch();
    }
}
