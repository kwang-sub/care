package com.example.care.membership.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MembershipHistoryCancelDTO {

    private Long userId;
    private Long membershipHistoryId;
}
