package hhplus.booking.app.concert.application.dto;

import hhplus.booking.app.concert.domain.entity.ConcertBooking;
import hhplus.booking.app.concert.domain.entity.ConcertSeat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

public class ConcertBookingInfo {

    public record Input(Long userId, Long concertSeatId) {}

    @Builder
    @Schema(name = "ConcertBookingOutput")
    public record Output(
            Long concertBookingId,
            Long userId,
            Long concertSeatId
    ) { public Output(ConcertBooking concertBooking) {
            this(
                    concertBooking.getConcertBookingId(),
                    concertBooking.getUserId(),
                    concertBooking.getConcertSeatId()
            );
        }
    }
}