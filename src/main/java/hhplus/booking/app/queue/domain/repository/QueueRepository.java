package hhplus.booking.app.queue.domain.repository;

import hhplus.booking.app.queue.domain.entity.Queue;

public interface QueueRepository {
    Queue getUser(Long userId);
}
