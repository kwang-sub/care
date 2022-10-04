package com.example.care.reserve.repository;

import com.example.care.membership.domain.MembershipStatus;
import com.example.care.membership.domain.QMembershipHistory;
import com.example.care.product.domain.ProductCode;
import com.example.care.reserve.domain.Reserve;
import com.example.care.reserve.dto.ReserveTimeRequestDTO;
import com.example.care.user.domain.QUser;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.example.care.membership.domain.QMembershipHistory.membershipHistory;
import static com.example.care.product.domain.QMembershipProduct.membershipProduct;
import static com.example.care.product.domain.QProduct.product;
import static com.example.care.reserve.domain.QReserve.reserve;
import static com.example.care.user.domain.QUser.user;

@RequiredArgsConstructor
public class ReserveRepositoryImpl implements ReserveRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Tuple findUseProductNum(ProductCode productCode, Long userId) {
        return queryFactory.select(reserve.count(), membershipProduct.maxNum.max())
                .from(reserve)
                .join(reserve.product, product)
                .join(product.membershipProductList, membershipProduct)
                .where(reserve.user.id.eq(userId),
                        product.code.eq(productCode))
                .groupBy(reserve.user)
                .fetchOne();
    }

    @Override
    public List<Reserve> findProductReserve(ReserveTimeRequestDTO reserveTimeRequestDTO) {
        return queryFactory.selectFrom(reserve)
                .join(reserve.product, product)
                .where(product.code.eq(reserveTimeRequestDTO.getProductCode()),
                        reserve.reserveDate.eq(reserveTimeRequestDTO.getReserveDate()),
                        eqReserveTime(reserveTimeRequestDTO.getReserveTime()))
                .fetch();
    }

    private BooleanExpression eqReserveTime(Integer time) {
        return time == null ? null : reserve.ReserveTime.eq(time);
    }
}
