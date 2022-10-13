package com.example.care.reserve.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class ReserveTimeResponseDTO {
    private int startTime;
    private int endTime;
    private Set<Integer> reserveTimeList = new HashSet<>();
}
