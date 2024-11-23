package hhplus.booking.app.concert.domain.repository;

import hhplus.booking.app.concert.domain.entity.Concert;
import hhplus.booking.app.concert.domain.entity.ConcertBooking;
import hhplus.booking.app.concert.domain.entity.ConcertSchedule;
import hhplus.booking.app.concert.domain.entity.ConcertSeat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;

public interface ConcertRepository {

    Concert getConcert(Long concertId);

    ConcertSchedule getConcertSchedule(Long concertScheduleId);

    ConcertSeat getConcertSeat(Long concertSeatId);

    List<ConcertSchedule> getConcertSchedules(Long concertId);

    List<ConcertSeat> getAvailableConcertSeats(Long concertScheduleId);

    ConcertBooking bookConcertSeat(Long userId, Long concertSeatId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    ConcertBooking getConcertBooking(Long concertBooingId);

    List<ConcertBooking> getExpiredBookings();
}
