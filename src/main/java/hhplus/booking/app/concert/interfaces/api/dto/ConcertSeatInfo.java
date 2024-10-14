package hhplus.booking.app.concert.interfaces.api.dto;

public class ConcertSeatInfo {
    public record Input(Long concertScheduleId) {}
    public record Output(Long concertId, Long concertSeatId) {}
}