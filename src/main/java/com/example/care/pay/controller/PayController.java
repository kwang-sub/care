package com.example.care.pay.controller;

import com.example.care.pay.dto.KaKaoPayDTO;
import com.example.care.util.SweetAlert.SwalIcon;
import com.example.care.util.SweetAlert.SwalMessage;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    @PostMapping("/pay/kakao")
    public String kakaoPayStart(KaKaoPayDTO kaKaoPayDTO, Principal principal, RedirectAttributes redirectAttributes) {
        String userId = principal.getName();
        System.out.println(kaKaoPayDTO);

        HttpURLConnection connection = null;//커넥션 얻기
        try {
            URL requestURL = new URL("https://kapi.kakao.com/v1/payment/ready");//요청주소
            connection = (HttpURLConnection) requestURL.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "KakaoAK 014d1017b411e3c1064db20f813b7d7e");
            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            connection.setDoOutput(true);

            connection.setRequestProperty("cid", "TCSUBSCRIP"); //가맹점코드
            connection.setRequestProperty("sid", "S1234567890987654321"); //정기결제 키
            connection.setRequestProperty("partner_order_id", ""); //가맹점 주문번호
            connection.setRequestProperty("partner_user_id", userId); //가맹점회원 id
            connection.setRequestProperty("item_name", kaKaoPayDTO.getGrade().name()); //상품명
            connection.setRequestProperty("quantity", "1"); //상품수량
            connection.setRequestProperty("total_amount", kaKaoPayDTO.getPrice().toString()); //상품총액
            connection.setRequestProperty("tax_free_amount", "0"); //상품 비과세 금액

            String parameter = " ";

            OutputStream outputStream = connection.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(parameter);
            dataOutputStream.flush();
            dataOutputStream.close();
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("swal",
                    new SwalMessage("결제 실패", "결제에 실패하였습니다. 다시 시도해주세요", SwalIcon.ERROR));
            return "redirect:/membership";
        }

        return null;
    }
}
