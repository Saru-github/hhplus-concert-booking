package hhplus.booking.app.queue.application;

import hhplus.booking.app.queue.domain.entity.Queue;
import hhplus.booking.app.queue.domain.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class QueueSchedulerService {

    private final QueueRepository queueRepository;

    private final static long MAX_QUEUE_SIZE = 10;

    // 대기열 WAITING -> PROCESSING 스케쥴러
    @Transactional
    @Scheduled(cron = "0 */1 * * * *", zone = "Asia/Seoul")
    public List<Queue> enterProcessingScheduler() {

        log.info(" ===== 대기열 WAITING -> PROCESSING 스케쥴러 시작 ===== ");

        long processingQueueQty = queueRepository.getProcessingQueueCount();
        long targetSize = MAX_QUEUE_SIZE - processingQueueQty;

        List<Queue> queues = queueRepository.findWaitingQueues().stream()
                .limit(targetSize)
                .peek(Queue::enterProcessing)
                .toList();

        log.info("입장 대기열 수: {}", queues.size());

        return queues;

    }

    // 만료된 대기열 삭제 스케쥴러
    @Transactional
    public List<Queue> deleteQueueScheduler() {

        log.info(" ===== 만료된 대기열 삭제 스케쥴러 시작 ===== ");

        List<Queue> expiredQueues = queueRepository.findDeleteExpiredQueues();
        queueRepository.deleteExpiredQueues();

        log.info("삭제된 대기열 수: {}", expiredQueues.size());

        return expiredQueues;
    }

    @Transactional
    @Scheduled(cron = "0 */1 * * * *", zone = "Asia/Seoul")
    public void execute() {
        enterProcessingScheduler();
        deleteQueueScheduler();
    }
}
