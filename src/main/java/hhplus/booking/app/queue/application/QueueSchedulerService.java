package hhplus.booking.app.queue.application;

import hhplus.booking.app.queue.domain.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class QueueSchedulerService {

    private final QueueRepository queueRepository;

    private final static long MAX_QUEUE_SIZE = 100;

    // 대기열 WAITING -> PROCESSING 스케쥴러
    @Transactional
    @Scheduled(cron = "0 */1 * * * *", zone = "Asia/Seoul")
    public void enterProcessingScheduler() {

        log.info(" ===== 대기열 WAITING -> PROCESSING 스케쥴러 시작 ===== ");

        long processingQueueQty = queueRepository.getProcessingQueueCount();
        long updateCount = MAX_QUEUE_SIZE - processingQueueQty;
        long updatedTokenCount = 0;
        if (updateCount > 0) {
            updatedTokenCount = queueRepository.activateWaitingToken(updateCount - 1);
        }

        log.info("현재 PROCESSING 토큰 수: {}, WAITING -> PROCESSING 토큰 수: {}",processingQueueQty, updatedTokenCount);

    }

    @Transactional
    @Scheduled(cron = "*/30 * * * * *", zone = "Asia/Seoul")
    public void execute() {
        enterProcessingScheduler();
    }
}
