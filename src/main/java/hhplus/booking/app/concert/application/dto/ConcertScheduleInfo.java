package hhplus.booking.app.concert.application.dto;

import java.time.LocalDate;

public class ConcertScheduleInfo {
    public record Input(Long concertId) {}
    public record Output(Long concertScheduleId, LocalDate concertDate) {}
}
