package com.example.care.product.dto;

import com.example.care.product.domain.ProductCode;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
public class ProductDTO {

    private Long id;
    private ProductCode code;
    private String title;
    private String description;
    private Integer maxNum;

    @Builder
    public ProductDTO(Long id, ProductCode code, String title, String description, Integer maxNum) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.description = description;
        this.maxNum = maxNum;
    }
}
