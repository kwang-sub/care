package com.example.care.reserve.dto;

import com.example.care.product.dto.ProductDTO;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
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
    private Integer reserveTime;
    private String postcode;
    private String address;
    private String detailAddress;
    private String extraAddress;
    private LocalDateTime regDate;

    private ProductDTO productDTO;

    @Builder
    public ReserveDTO(Long id, String name, String phoneNumber, LocalDate reserveDate, Integer reserveTime, String postcode,
                      String address, String detailAddress, String extraAddress, LocalDateTime regDate, ProductDTO productDTO) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.reserveDate = reserveDate;
        this.reserveTime = reserveTime;
        this.postcode = postcode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.extraAddress = extraAddress;
        this.regDate = regDate;
        this.productDTO = productDTO;
    }
}
