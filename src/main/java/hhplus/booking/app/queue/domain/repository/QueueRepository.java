package hhplus.booking.app.queue.domain.repository;

import hhplus.booking.app.queue.domain.entity.Queue;

import java.util.List;

public interface QueueRepository {
    Queue registerQueue();

    Queue getQueue(String queueTokenValue);

    List<Queue> findWaitingQueues(String status);

    long getProcessingQueueCount();

    void deleteQueue(Long tokenId);

}
