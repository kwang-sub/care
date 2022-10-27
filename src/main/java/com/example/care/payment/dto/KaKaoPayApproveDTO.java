package com.example.care.payment.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class KaKaoPayApproveDTO {

    private String aid; //요청 고유 번호
    private String tid; //결제 고유 번호
    private String cid; //가맹점 코드
    private String sid; //정기 결제용 id
    private String partner_order_id;    //가맹점 주문 번호
    private String partner_user_id; //가맹점 회원 id
    private String payment_method_type; //결제 수단
    private Amount amount;  //결제 금액 정보
    private String item_name;   //상품 이름
    private LocalDateTime approved_at; //결제 승인 시각

    @Builder
    public KaKaoPayApproveDTO(String aid, String tid, String cid, String sid, String partner_order_id, String partner_user_id,
                              String payment_method_type, Amount amount, String item_name, LocalDateTime approved_at) {
        this.aid = aid;
        this.tid = tid;
        this.cid = cid;
        this.sid = sid;
        this.partner_order_id = partner_order_id;
        this.partner_user_id = partner_user_id;
        this.payment_method_type = payment_method_type;
        this.amount = amount;
        this.item_name = item_name;
        this.approved_at = approved_at;
    }
}
