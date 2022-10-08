package com.example.care.util.scheduler;

import com.example.care.reserve.service.ReserveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReserveScheduler {

    private final ReserveService reserveService;

    @Scheduled(cron = "0 0 * * * *")
    public void reserveCompleteSch() {
        LocalDateTime now = LocalDateTime.now();
        log.info("[Scheduler] reserveComplete time : {}", now);
        reserveService.reserveComplete(now);
    }

    @Scheduled(cron = "0 12 0 * * *")
    public void membershipCompleteSch() {
        LocalDateTime now = LocalDateTime.now();
        log.info("[Scheduler] membershipHistoryComplete time : {}", now);
        reserveService.reserveComplete(now);
    }
}
