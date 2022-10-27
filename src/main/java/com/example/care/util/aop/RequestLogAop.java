package com.example.care.util.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Aspect
@Component
public class RequestLogAop {

    @Around("execution(* com.example.care.*.controller..*.*(..))")
    public Object beforeRequestLog(ProceedingJoinPoint joinPoint) throws  Throwable {
        log.info("컨트롤러 요청 {}", joinPoint.getSignature().toString());
        try {
            Object result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            log.error("요청 예외", e);
            throw new RuntimeException(e);
        }
    }
}
