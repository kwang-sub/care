package com.example.care.membership.dto;

import com.example.care.membership.domain.Grade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class MembershipDTO {

    private Long id;
    private Grade grade;
    private Integer price;
    private List<MembershipDetailDTO> membershipDetailDTOS = new ArrayList<>();
}
