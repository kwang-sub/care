package com.example.care.membership.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MembershipDetailDTO {

    private Long id;
    private String title;
    private String comment;
    private Integer number;
}
