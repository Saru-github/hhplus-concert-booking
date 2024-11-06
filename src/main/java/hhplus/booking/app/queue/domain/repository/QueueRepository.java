package hhplus.booking.app.queue.domain.repository;

import hhplus.booking.app.queue.domain.entity.Queue;

import java.util.List;

public interface QueueRepository {
    Queue registerQueue();

    Queue getQueue(String queueTokenValue);


    long getProcessingQueueCount();

    long findWaitingQueues(String queueTokenValue);

    List<Queue> findDeleteExpiredQueues();

    void deleteExpiredQueues();

}
