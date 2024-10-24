package hhplus.booking.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("BusinessException 발생!!!! - 상태 코드: {}, 메시지: {}", e.getErrorCode(), e.getMessage(), e);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(new ErrorResponse(errorCode));
    }

    @ExceptionHandler(value = Exception.class)
        public ResponseEntity<ErrorResponse> handleException(Exception e) {
            log.error("에러 발생!!!! - 메시지: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(new ErrorResponse("500", e.getMessage()));
        }
}
