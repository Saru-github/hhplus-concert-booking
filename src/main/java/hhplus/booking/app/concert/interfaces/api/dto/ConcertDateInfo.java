package hhplus.booking.app.concert.interfaces.api.dto;

public class ConcertDateInfo {
    public record Input() {}
    public record Output(Long concertScheduleId, String concertDate) {}
}
