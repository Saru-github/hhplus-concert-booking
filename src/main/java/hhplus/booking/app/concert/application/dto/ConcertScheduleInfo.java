package hhplus.booking.app.concert.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public class ConcertScheduleInfo {

    public record Input(
            Long concertId
    ) {}

    @Schema(name = "ConcertScheduleOutput")
    public record Output(
            Long concertScheduleId,
            LocalDate concertDate
    ) {}
}
