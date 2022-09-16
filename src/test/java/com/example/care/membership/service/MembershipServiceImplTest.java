package com.example.care.membership.service;

import com.example.care.membership.domain.Grade;
import com.example.care.membership.dto.MembershipDTO;
import com.example.care.membership.dto.MembershipDetailDTO;
import com.example.care.membership.repository.MembershipRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MembershipServiceTest {

    @Autowired
    private MembershipService membershipService;
    @Autowired
    private MembershipRepository membershipRepository;

    @Test
    @DisplayName("멤버쉽 저장 테스트")
    void membershipSaveTest() {
        //given
        List<MembershipDetailDTO> membershipDetailDTOS = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            membershipDetailDTOS.add(MembershipDetailDTO.builder()
                    .title("서비스명")
                    .comment("설명")
                    .number(5)
                    .build());
        }

        MembershipDTO membershipDTO = MembershipDTO.builder()
                .grade(Grade.BRONZE)
                .price(10000)
                .membershipDetailDTOs(membershipDetailDTOS)
                .build();

        //when
        membershipService.membershipSave(membershipDTO);

        //then
        List<MembershipDTO> membershipDTOS = membershipService.membershipList();
        assertThat(membershipDTOS.size()).isEqualTo(1);

        MembershipDTO findDTO = membershipDTOS.get(0);
        assertThat(findDTO.getGrade()).isEqualTo(membershipDTO.getGrade());
        assertThat(findDTO.getMembershipDetailDTOs().size()).isEqualTo(3);
    }
}