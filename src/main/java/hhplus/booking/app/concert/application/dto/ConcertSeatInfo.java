package hhplus.booking.app.concert.application.dto;

import hhplus.booking.app.concert.domain.entity.ConcertSeat;
import lombok.Builder;

public class ConcertSeatInfo {
    public record Input(Long concertScheduleId) {}

    @Builder
    public record Output(
            Long concertSeatId,
            Long concertScheduleId,
            Long seatNumber,
            Long price
    ) { public Output(ConcertSeat concertSeat) {
            this(
                concertSeat.getConcertSeatId(),
                concertSeat.getConcertScheduleId(),
                concertSeat.getSeatNumber(),
                concertSeat.getPrice()
            );
        }
    }
}