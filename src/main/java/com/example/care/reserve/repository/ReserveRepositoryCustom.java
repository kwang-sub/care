package com.example.care.reserve.repository;

import com.example.care.product.domain.ProductCode;
import com.example.care.reserve.domain.Reserve;
import com.example.care.reserve.dto.ReserveConfirmDTO;
import com.querydsl.core.Tuple;

import java.util.List;

public interface ReserveRepositoryCustom {

    List<Reserve> findProductReserve(ReserveConfirmDTO reserveConfirmDTO);

    Tuple findUseProductNum(ProductCode productCode, Long userId);
}
