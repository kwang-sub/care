package com.example.care.reserve.repository;

import com.example.care.reserve.domain.Reserve;
import com.example.care.reserve.dto.ReserveConfirmDTO;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.care.product.domain.QProduct.product;
import static com.example.care.reserve.domain.QReserve.reserve;

@RequiredArgsConstructor
public class ReserveRepositoryImpl implements ReserveRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Reserve> findProductReserve(ReserveConfirmDTO reserveConfirmDTO) {
        return queryFactory.selectFrom(reserve)
                .join(reserve.product, product)
                .where(product.code.eq(reserveConfirmDTO.getProductCode()),
                        reserve.reserveDate.eq(reserveConfirmDTO.getReserveDate()),
                        eqReserveTime(reserveConfirmDTO.getReserveTime()))
                .fetch();
    }

    private BooleanExpression eqReserveTime(Integer time) {
        return time == null ? null : reserve.ReserveTime.eq(time);
    }
}
