package hhplus.booking.app.concert.infra;

import hhplus.booking.app.concert.domain.entity.ConcertBooking;
import hhplus.booking.app.concert.domain.entity.ConcertSchedule;
import hhplus.booking.app.concert.domain.entity.ConcertSeat;
import hhplus.booking.app.concert.domain.repository.ConcertRepository;
import hhplus.booking.app.concert.infra.jpa.ConcertBookingJpaRepository;
import hhplus.booking.app.concert.infra.jpa.ConcertScheduleJpaRepository;
import hhplus.booking.app.concert.infra.jpa.ConcertSeatJpaRepository;
import hhplus.booking.config.exception.BusinessException;
import hhplus.booking.config.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertScheduleJpaRepository concertScheduleJpaRepository;
    private final ConcertSeatJpaRepository concertSeatJpaRepository;
    private final ConcertBookingJpaRepository concertBookingJpaRepository;

    @Override
    public ConcertSeat getConcertSeat(Long concertSeatId) {
        return concertSeatJpaRepository.findById(concertSeatId).orElseThrow(() -> new BusinessException(ErrorCode.SEAT_NOT_FOUND));
    }

    @Override
    public List<ConcertSchedule> getConcertSchedules(Long concertId) {
        return concertScheduleJpaRepository.findAllByConcertId(concertId);
    }

    @Override
    public List<ConcertSeat> getAvailableConcertSeats(Long concertScheduleId) {
        return concertSeatJpaRepository.findAllByStatusAndConcertScheduleId("AVAILABLE", concertScheduleId);
    }

    @Override
    public ConcertBooking bookConcertSeat(Long userId, Long concertSeatId) {
        return concertBookingJpaRepository.save(ConcertBooking.of(userId, concertSeatId));
    }

    @Override
    public ConcertBooking getConcertBooking(Long concertBooingId) {
        return concertBookingJpaRepository.findById(concertBooingId).orElseThrow(() -> new BusinessException(ErrorCode.BOOKING_NOT_FOUND));
    }

    @Override
    public List<ConcertBooking> getExpiredBookings() {
        return concertBookingJpaRepository.findByStatusAndExpiredAtBefore("BOOKED", LocalDateTime.now());
    }
}
