package hhplus.booking.app.concert.domain.repository;

import hhplus.booking.app.concert.domain.entity.ConcertSchedule;
import hhplus.booking.app.concert.domain.entity.ConcertSeat;

import java.util.List;

public interface ConcertRepository {
    List<ConcertSchedule> getConcertSchedule(Long concertId);
    List<ConcertSeat> getConcertSeat(Long concertScheduleId);
}
