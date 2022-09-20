package com.example.care.membership.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;

class MembershipHistoryTest {

    @Test
    @DisplayName("멤버쉽 탈퇴시 만료 날짜 계산 테스트")
    void test() {

        LocalDateTime regDate;
        LocalDate now;
        LocalDate endDate;

//      동일일 테스트
        regDate = LocalDateTime.of(2022, 1, 15, 2, 10);
        now = LocalDate.of(2022, 11, 15);
        endDate = cancelDateLogic(regDate, now);
        assertThat(endDate).isEqualTo(LocalDate.of(2022, 11, 15));

//      현재 이전일 테스트
        regDate = LocalDateTime.of(2022, 1, 10, 2, 10);
        now = LocalDate.of(2022, 11, 15);
        endDate = cancelDateLogic(regDate, now);
        assertThat(endDate).isEqualTo(LocalDate.of(2022, 12, 10));

//      현재 이후일 테스트
        regDate = LocalDateTime.of(2022, 1, 15, 2, 10);
        now = LocalDate.of(2022, 11, 10);
        endDate = cancelDateLogic(regDate, now);

        assertThat(endDate).isEqualTo(LocalDate.of(2022, 11, 15));

//      12월 테스트
        regDate = LocalDateTime.of(2022, 2, 28, 2, 10);
        now = LocalDate.of(2022, 12, 31);
        endDate = cancelDateLogic(regDate, now);

        assertThat(endDate).isEqualTo(LocalDate.of(2023, 1, 28));

//      말일 테스트
        regDate = LocalDateTime.of(2022, 1, 31, 2, 10);
        now = LocalDate.of(2022, 2, 20);
        endDate = cancelDateLogic(regDate, now);

        assertThat(endDate).isEqualTo(LocalDate.of(2022, 2, 28));
    }

    private static LocalDate cancelDateLogic(LocalDateTime regDate, LocalDate now) {
        LocalDate endDate;
        if (now.getMonthValue() == 12) {
            if (regDate.getDayOfMonth() >= now.getDayOfMonth()) {
                endDate = LocalDate.of(now.getYear(), 12, regDate.getDayOfMonth());
            } else {
                endDate = LocalDate.of(now.getYear() + 1, 1, regDate.getDayOfMonth());
            }

        } else if (regDate.getDayOfMonth() >= now.getDayOfMonth()) {
            if (regDate.getDayOfMonth() >= now.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth()){
                endDate = now.with(TemporalAdjusters.lastDayOfMonth());
            } else{
                endDate = LocalDate.of(now.getYear(), now.getMonth(), regDate.getDayOfMonth());
            }
        } else {
            endDate = LocalDate.of(now.getYear(), now.getMonthValue() + 1, regDate.getDayOfMonth());
        }
        return endDate;
    }

}