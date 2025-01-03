package org.example.expert.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class AdminApiInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 인증 및 권한 확인
        String userRole = request.getHeader("User-Role");
        if (!"ADMIN".equalsIgnoreCase(userRole)) {
            log.warn("Unauthorized access attempt by user. URL: {}", request.getRequestURI());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return false;
        }

        log.info("Admin API accessed. URL: {}, Time: {}", request.getRequestURI(), System.currentTimeMillis());
        return true;
    }
}
