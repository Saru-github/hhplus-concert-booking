package hhplus.booking.app.booking.interfaces.api.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class PaymentBookingInfo {
    public record Input(Long bookingId) {}
    @Builder
    public record Output(
            Long bookingId,
            Long userId,
            Long concertId,
            Long concertScheduleId,
            Long concertSeatId,
            Long amountUsed,
            LocalDateTime expiredAt,
            String status) {}
}
