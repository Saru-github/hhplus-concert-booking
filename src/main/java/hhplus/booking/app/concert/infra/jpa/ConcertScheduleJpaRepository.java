package hhplus.booking.app.concert.infra.jpa;

import hhplus.booking.app.concert.domain.entity.ConcertSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcertScheduleJpaRepository extends JpaRepository<ConcertSchedule, Long> {
    List<ConcertSchedule> findAllByConcertId(Long concertId);
}
