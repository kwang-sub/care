package com.example.care.reserve.dto;

import com.example.care.product.dto.ProductDTO;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ToString
public class ReserveDTO {
    private Long id;
    @NotBlank
    @Pattern(regexp = "^[가-힣a-zA-Z]*$")
    private String name;
    @NotBlank
    @Pattern(regexp = "\\d{2,3}-\\d{3,4}-\\d{4}")
    private String phoneNumber;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reserveDate;
    @NotNull
    @Range(min = 0, max = 24)
    private Integer ReserveTime;
    private ProductDTO productDTO;
    private String postcode;
    private String address;
    private String detailAddress;
    private String extraAddress;
    private LocalDateTime regDate;
}
