package hhplus.booking.app.queue.domain.repository;

public interface QueueRepository {
    String registerQueue();

    Long getQueueRank(String queueTokenValue);

    long getProcessingQueueCount();

    long activateWaitingToken(long updateCount);

    void deleteProcessingToken(String processingToken);

}
