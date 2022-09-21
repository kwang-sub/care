package com.example.care.membership.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;

import static org.assertj.core.api.Assertions.assertThat;

class MembershipHistoryTest {

    @Test
    @DisplayName("멤버쉽 만료 날짜 계산 테스트")
    void test() {

        LocalDate regDate;
        LocalDate endDate;

//      평일 테스트
        regDate = LocalDate.of(2022, 1, 15);
        endDate = cancelDateLogic(regDate);
        assertThat(endDate).isEqualTo(LocalDate.of(2022, 2, 15));

//      년말 테스트
        regDate = LocalDate.of(2022, 12, 31);
        endDate = cancelDateLogic(regDate);
        assertThat(endDate).isEqualTo(LocalDate.of(2023, 1, 31));

//      2월테스트
        regDate = LocalDate.of(2022, 1, 31);
        endDate = cancelDateLogic(regDate);

        assertThat(endDate).isEqualTo(LocalDate.of(2022, 2, 28));

    }

    private static LocalDate cancelDateLogic(LocalDate regDate) {
        LocalDate endDate = null;
        LocalDate now = regDate;
        if (now.getMonthValue() == 12) {
            endDate = LocalDate.of(now.getYear() + 1, 1, now.getDayOfMonth());
            return endDate;
        }

        LocalDate next = LocalDate.of(now.getYear(), now.getMonthValue() + 1, 1);

        if (now.getDayOfMonth() > next.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth()) {
            endDate = next.with(TemporalAdjusters.lastDayOfMonth());
        } else {
            endDate = LocalDate.of(now.getYear(), now.getMonthValue() + 1, now.getDayOfMonth());
        }
        return endDate;
    }

}