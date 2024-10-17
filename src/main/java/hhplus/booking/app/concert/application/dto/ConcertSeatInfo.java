package hhplus.booking.app.concert.application.dto;

import hhplus.booking.app.concert.domain.entity.ConcertSeat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public class ConcertSeatInfo {

    public record Input(
            Long concertScheduleId
    ) {}

    @Schema(name = "ConcertSeatOutput")
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