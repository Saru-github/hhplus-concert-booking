package hhplus.booking.interfaces.concert.dto;

import java.time.LocalDate;

public class ConcertDateInfo {
    public record Input() {}
    public record Output(Long concertScheduleId, String concertDate) {}
}
