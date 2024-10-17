package hhplus.booking.app.concert.infra.jpa;

import hhplus.booking.app.concert.domain.entity.ConcertSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeat, Long> {
    List<ConcertSeat> findAllByConcertScheduleIdAndStatus(Long concertScheduleId, String status);
}
