package org.example.expert.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
public class AdminApiLoggingAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("execution(* org.example.expert.domain.comment.controller.CommentAdminController.*(..)) || " +
            "execution(* org.example.expert.domain.user.controller.UserAdminController.*(..))")
    public Object logAdminApi(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();
        String requestPayload = objectMapper.writeValueAsString(args);

        log.info("Admin API Accessed: Method: {}, Args: {}, Time: {}",
                joinPoint.getSignature().toShortString(), requestPayload, LocalDateTime.now());

        Object result = joinPoint.proceed();

        String responsePayload = objectMapper.writeValueAsString(result);
        log.info("Admin API Response: Method: {}, Response: {}, Time: {}",
                joinPoint.getSignature().toShortString(), responsePayload, LocalDateTime.now());

        return result;
    }
}
