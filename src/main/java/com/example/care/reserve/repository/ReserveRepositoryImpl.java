package com.example.care.reserve.repository;

import com.example.care.reserve.domain.Reserve;
import com.example.care.reserve.dto.ReserveTimeRequestDTO;
import com.example.care.util.pagin.PageRequestDTO;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.care.product.domain.QProduct.product;
import static com.example.care.reserve.domain.QReserve.reserve;

@RequiredArgsConstructor
public class ReserveRepositoryImpl implements ReserveRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Reserve> findProductReserve(ReserveTimeRequestDTO reserveTimeRequestDTO) {
        return queryFactory.selectFrom(reserve)
                .join(reserve.product, product).fetchJoin()
                .where(product.code.eq(reserveTimeRequestDTO.getProductCode()),
                        reserve.reserveDate.eq(reserveTimeRequestDTO.getReserveDate()),
                        eqReserveTime(reserveTimeRequestDTO.getReserveTime()))
                .fetch();
    }

    @Override
    public Page<Reserve> findUserReserveList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable();
        Long userId = Long.valueOf(pageRequestDTO.getKeyword());
        List<Reserve> content = queryFactory.selectFrom(reserve)
                .join(reserve.product, product).fetchJoin()
                .where(reserve.user.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(reserve.id.desc())
                .fetch();

        Long count = queryFactory.select(reserve.count())
                .from(reserve)
                .where(reserve.user.id.eq(userId))
                .fetchOne();

        return PageableExecutionUtils.getPage(content, pageable, () -> count);
    }

    private BooleanExpression eqReserveTime(Integer time) {
        return time == null ? null : reserve.ReserveTime.eq(time);
    }
}
