package com.example.care.product.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ToString
public class ReserveDTO {
    @NotBlank
    @Pattern(regexp = "^[가-힣a-zA-Z]*$")
    private String name;
    @NotBlank
    @Pattern(regexp = "^01(?:0|1|[6-9]) - (?:\\d{3}|\\d{4}) - \\d{4}$")
    private String phoneNumber;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reserveDate;
    @Min(0)
    @Max(24)
    private Integer ReserveTime;
    private ProductDTO productDTO;
    private String postcode;
    private String address;
    private String detailAddress;
    private String extraAddress;
    private LocalDateTime regDate;
}
