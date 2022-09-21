package com.example.care.membership.dto;

import com.example.care.membership.domain.Grade;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MembershipDTO {

    private Long id;
    private Grade grade;
    private Integer price;
    private Integer transportNum;
    private Integer cleanNum;
    private Integer counselNum;
}
