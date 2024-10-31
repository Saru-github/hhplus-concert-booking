package hhplus.booking.config.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    TOKEN_NOT_FOUND(200, "토큰 정보가 존재하지 않습니다."),
    INVALID_TOKEN(200, "유효하지 않은 토큰입니다."),
    TOKEN_NOT_PROCESSING(200, "토큰이 활성화 된 상태가 아닙니다."),

    CONCERT_NOT_FOUND(200, "콘서트 정보를 찾을 수 없습니다."),
    SCHEDULE_NOT_FOUND(200, "콘서트 일정을 찾을 수 없습니다."),
    SEAT_NOT_FOUND(200, "좌석 정보 찾을 수 없습니다."),
    SEAT_UNAVAILABLE(200, "예약 가능한 좌석이 아닙니다."),
    SEAT_ALREADY_BOOKED(200, "해당 좌석이 이미 예약되었습니다."),

    USER_NOT_FOUND(200, "존재하지 않는 사용자입니다."),
    INSUFFICIENT_USER_POINTS(200, "남은 잔액이 부족합니다."),
    INVALID_POINT_REQUEST(200, "요청 포인트는 0 이하 일 수 없습니다."),

    BOOKING_NOT_FOUND(200, "예약 정보 찾을 수 없습니다."),
    PAYMENT_NOT_ALLOWED(200, "결제가 불가능한 예약입니다."),
    PAYMENT_FAILED_AMOUNT(200, "결제 잔액이 부족합니다."),
    POINT_DUPLICATE_REQUEST(200, "포인트 중복 요청입니다."),

    INTERNAL_SERVER_ERROR(500, "서버 에러가 발생하였습니다.");

    private final int httpStatus;
    private final String message;

    ErrorCode(int httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }



}
