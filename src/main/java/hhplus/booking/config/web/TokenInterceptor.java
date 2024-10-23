package hhplus.booking.config.web;

import hhplus.booking.app.queue.domain.repository.QueueRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenInterceptor implements HandlerInterceptor {

    private final QueueRepository queueRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("요청 URL: {}", request.getRequestURI());

        String tokenValue = request.getHeader("Authorization");

        log.info("tokenValue: {}", tokenValue);

        if (tokenValue == null || tokenValue.isBlank()) {
            throw new IllegalArgumentException("헤더 정보를 찾을 수 없습니다.");
        }

        tokenValue = tokenValue.substring(7);

        String tokenStatus = queueRepository.getQueue(tokenValue).getStatus();

        if (!"PROCESSING".equals(tokenStatus)) {
            throw new IllegalStateException("토큰이 활성화 된 상태가 아닙니다.");
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}
