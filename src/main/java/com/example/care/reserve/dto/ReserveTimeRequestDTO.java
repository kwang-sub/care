package com.example.care.reserve.dto;

import com.example.care.product.domain.ProductCode;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
public class ReserveTimeRequestDTO {

    private ProductCode productCode;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reserveDate;
    private Integer reserveTime;
}
