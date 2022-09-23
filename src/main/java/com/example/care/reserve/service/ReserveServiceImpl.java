package com.example.care.reserve.service;

import com.example.care.reserve.dto.ReserveConfirmDTO;
import com.example.care.reserve.dto.ReserveDTO;
import com.example.care.reserve.repository.ReserveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReserveServiceImpl implements ReserveService{

    private final ReserveRepository reserveRepository;
    @Override
    public void reserve(ReserveDTO reserveDTO) {
//        같은 시간대 서비스 예약 있는지 확인하는 로직
        ReserveConfirmDTO reserveConfirmDTO = ReserveConfirmDTO.builder()
                .reserveDate(reserveDTO.getReserveDate())
                .reserveTime(reserveDTO.getReserveTime())
                .productCode(reserveDTO.getProductDTO().getCode())
                .build();

        reserveRepository.findProductReserve(reserveConfirmDTO);

    }
}
