package hhplus.booking.app.queue.domain.repository;

import hhplus.booking.app.queue.domain.entity.Queue;

import java.util.List;

public interface QueueRepository {
    String registerQueue();

    Long getQueueRank(String queueTokenValue);

    long getProcessingQueueCount();

    List<Queue> findDeleteExpiredQueues();

    void deleteExpiredQueues();

}
