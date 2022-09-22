package com.example.care.product.dto;

import com.example.care.product.domain.ProductCode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDTO {

    private Long id;
    private ProductCode code;
    private String title;
    private String description;
    private Integer maxNum;
}
