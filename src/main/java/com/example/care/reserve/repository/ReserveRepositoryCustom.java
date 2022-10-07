package com.example.care.reserve.repository;

import com.example.care.product.domain.ProductCode;
import com.example.care.reserve.domain.Reserve;
import com.example.care.reserve.dto.ReserveTimeRequestDTO;
import com.example.care.util.pagin.PageRequestDTO;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReserveRepositoryCustom {

    List<Reserve> findProductReserve(ReserveTimeRequestDTO reserveTimeRequestDTO);

    Page<Reserve> findUserReserveList(PageRequestDTO pageRequestDTO);

    Reserve findByIdWithCancel(Long reserveId);
}
