package org.example.expert.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Slf4j
@Component
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
            ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);

            chain.doFilter(wrappedRequest, wrappedResponse);

            String requestBody = new String(wrappedRequest.getContentAsByteArray());
            log.info("Request Body: {}", requestBody);

            String responseBody = new String(wrappedResponse.getContentAsByteArray());
            log.info("Response Body: {}", responseBody);

            wrappedResponse.copyBodyToResponse(); // 응답 본문 복원
        } else {
            chain.doFilter(request, response);
        }
    }
}
