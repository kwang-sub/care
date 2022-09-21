package com.example.care.payment.dto;

import com.example.care.membership.domain.Grade;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MemberShipDTO {
    public Grade grade;
    public Integer price;
}
