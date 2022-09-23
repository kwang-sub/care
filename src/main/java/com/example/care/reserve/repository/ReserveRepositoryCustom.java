package com.example.care.reserve.repository;

import com.example.care.reserve.domain.Reserve;
import com.example.care.reserve.dto.ReserveConfirmDTO;

import java.util.List;

public interface ReserveRepositoryCustom {

    List<Reserve> findProductReserve(ReserveConfirmDTO reserveConfirmDTO);
}
