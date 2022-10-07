package com.example.care.reserve.service;

import com.example.care.reserve.domain.Reserve;
import com.example.care.reserve.dto.*;
import com.example.care.util.pagin.PageRequestDTO;
import com.example.care.util.pagin.PageResultDTO;

import java.time.LocalDateTime;

public interface ReserveService {


    void reserve(ReserveDTO reserveDTO, Long userId);

    ReserveTimeResponseDTO confirmReserveTime(ReserveTimeRequestDTO reserveTimeRequestDTO);

    PageResultDTO<ReserveListDTO, Reserve> getReserveList(PageRequestDTO pageRequestDTO);

    void reserveCancel(Long reserveId);

    void reserveComplete(LocalDateTime now);
}
