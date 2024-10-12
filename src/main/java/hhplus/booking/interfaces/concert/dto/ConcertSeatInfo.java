package hhplus.booking.interfaces.concert.dto;

public class ConcertSeatInfo {
    public record Input(Long concertScheduleId) {}
    public record Output(Long concertId, Long concertSeatId) {}
}
