package hhplus.booking.app.concert.domain.repository;

import hhplus.booking.app.concert.domain.entity.ConcertBooking;
import hhplus.booking.app.concert.domain.entity.ConcertSchedule;
import hhplus.booking.app.concert.domain.entity.ConcertSeat;

import java.util.List;

public interface ConcertRepository {

    ConcertSeat getConcertSeat(Long concertSeatId);

    List<ConcertSchedule> getConcertSchedules(Long concertId);

    List<ConcertSeat> getAvailableConcertSeats(Long concertScheduleId);

    ConcertBooking bookConcertSeat(Long userId, Long concertSeatId);

    ConcertBooking getConcertBooking(Long concertBooingId);

    List<ConcertBooking> getExpiredBookings();
}
