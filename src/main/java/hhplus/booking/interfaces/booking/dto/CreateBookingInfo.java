package hhplus.booking.interfaces.booking.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class CreateBookingInfo {
    public record Input(Long userId, Long seatId) {}
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
