package com.example.care.util.scheduler;

import com.example.care.payment.service.PaymentService;
import com.example.care.reserve.service.ReserveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class CareScheduler {

    private final ReserveService reserveService;
    private final PaymentService paymentService;

    @Scheduled(cron = "0 0 * * * *")
    public void reserveCompleteSch() {
        LocalDateTime now = LocalDateTime.now();
        log.info("[Scheduler] reserveComplete time : {}", now);
        reserveService.reserveCompleteSch(now);
    }

    @Scheduled(cron = "5 0 8 * * *")
    public void membershipCompleteSch() {
        LocalDate now = LocalDate.now();
        log.info("[Scheduler] paymentRegular time : {}", now);
        paymentService.membershipCompleteSch(now);
    }
}
