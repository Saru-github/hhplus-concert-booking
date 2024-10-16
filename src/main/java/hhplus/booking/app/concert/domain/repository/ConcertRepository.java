package hhplus.booking.app.concert.domain.repository;

import hhplus.booking.app.concert.domain.entity.ConcertBooking;
import hhplus.booking.app.concert.domain.entity.ConcertSchedule;
import hhplus.booking.app.concert.domain.entity.ConcertSeat;

import java.util.List;

public interface ConcertRepository {
    List<ConcertSchedule> getConcertSchedules(Long concertId);
    List<ConcertSeat> getConcertSeats(Long concertScheduleId);
    ConcertBooking bookConcertSeat(Long userId, Long concertSeatId);
}
