package hhplus.booking.app.concert.infra;

import hhplus.booking.app.concert.domain.entity.ConcertSchedule;
import hhplus.booking.app.concert.domain.entity.ConcertSeat;
import hhplus.booking.app.concert.domain.repository.ConcertRepository;
import hhplus.booking.app.concert.infra.jpa.ConcertScheduleJpaRepository;
import hhplus.booking.app.concert.infra.jpa.ConcertSeatJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertScheduleJpaRepository concertScheduleJpaRepository;
    private final ConcertSeatJpaRepository concertSeatJpaRepository;

    @Override
    public List<ConcertSchedule> getConcertSchedule(Long concertId) {
        return concertScheduleJpaRepository.findAllByConcertId(concertId);
    }

    @Override
    public List<ConcertSeat> getConcertSeat(Long concertScheduleId) {
        return concertSeatJpaRepository.findAllByConcertScheduleId(concertScheduleId);
    }
}
