package com.example.care.reserve.service;

import com.example.care.reserve.dto.ReserveDTO;

public interface ReserveService {


    void reserve(ReserveDTO reserveDTO, Long userId);
}
