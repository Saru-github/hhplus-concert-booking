package hhplus.booking.app.scheduler;

import hhplus.booking.app.queue.domain.entity.Queue;
import hhplus.booking.app.queue.domain.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QueueScheduler {
    // TODO: 대기열 WAITING -> PROCESSING, 만료 등 스케쥴러

    private final QueueRepository queueRepository;

    public final long MAX_QUEUE_SIZE = 10;

    public List<Queue> enterProcessingScheduler() {

        long processingQueueQty = queueRepository.getProcessingQueueCount();
        long targetSize = MAX_QUEUE_SIZE - processingQueueQty;

        return queueRepository.findWaitingQueues("WAITING").stream()
                .limit(targetSize)
                .peek(Queue::enterProcessing)
                .toList();
    }
}
