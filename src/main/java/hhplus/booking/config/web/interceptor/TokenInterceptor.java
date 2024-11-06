package hhplus.booking.config.web.interceptor;

import hhplus.booking.app.queue.domain.repository.QueueRepository;
import hhplus.booking.config.exception.BusinessException;
import hhplus.booking.config.exception.ErrorCode;
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

        String tokenValue = request.getHeader("Authorization");
        log.info("요청 URL: {}, 토큰 값: {}", request.getRequestURI(), tokenValue);

        if (tokenValue == null || tokenValue.isBlank()) {
            throw new BusinessException(ErrorCode.TOKEN_NOT_FOUND);
        }
//
//        tokenValue = tokenValue.substring(7);
//
//        String tokenStatus = queueRepository.getQueue(tokenValue).getStatus();
//
//        if (!"PROCESSING".equals(tokenStatus)) {
//            throw new BusinessException(ErrorCode.TOKEN_NOT_PROCESSING);
//        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}
