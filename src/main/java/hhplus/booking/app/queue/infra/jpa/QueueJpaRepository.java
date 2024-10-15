package hhplus.booking.app.queue.infra.jpa;

import hhplus.booking.app.queue.domain.entity.Queue;
import hhplus.booking.app.queue.domain.repository.QueueRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueueJpaRepository extends JpaRepository<Queue, Long>, QueueRepository {
}
