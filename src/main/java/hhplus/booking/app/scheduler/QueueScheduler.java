package hhplus.booking.app.scheduler;

import hhplus.booking.app.concert.domain.entity.ConcertBooking;
import hhplus.booking.app.concert.domain.repository.ConcertRepository;
import hhplus.booking.app.queue.domain.entity.Queue;
import hhplus.booking.app.queue.domain.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QueueScheduler {

    private final QueueRepository queueRepository;
    private final ConcertRepository concertRepository;
    private static final Logger logger = LoggerFactory.getLogger(QueueScheduler.class);

    public final long MAX_QUEUE_SIZE = 10;

    // TODO: 대기열 WAITING -> PROCESSING, 만료 등 스케쥴러
    @Transactional
    public List<Queue> enterProcessingScheduler() {

        long processingQueueQty = queueRepository.getProcessingQueueCount();
        long targetSize = MAX_QUEUE_SIZE - processingQueueQty;

        List<Queue> queues = queueRepository.findWaitingQueues("WAITING").stream()
                .limit(targetSize)
                .peek(Queue::enterProcessing)
                .toList();

        logger.info("Processing scheduler completed. Updated {} queues to PROCESSING status.", queues.size());

        return queues;
    }

    // TODO: 좌석 임시 예약 -> 예약해제, 임시 예약 만료 등 스케쥴러
    @Transactional
    public List<ConcertBooking> expiredConcertBookingScheduler() {
        List<ConcertBooking> expiredConcertBookings =  concertRepository.getExpiredBookings();
        for (ConcertBooking expiredConcertBooking : expiredConcertBookings) {
            expiredConcertBooking.expiredBookingStatus();
            concertRepository.getConcertSeat(expiredConcertBooking.getConcertSeatId()).updateSeatStatusToAvailable();

            logger.info("Expired concert booking scheduler completed. {} bookings expired.", expiredConcertBookings.size());

        }

        return expiredConcertBookings;
    }

    // TODO: 임시예약 만료, 결제 완료 시 대기열 삭제 스케쥴러

    @Transactional
    public List<Queue> deleteQueueScheduler() {
        List<Queue> expiredQueues = queueRepository.findDeleteExpiredQueues();
        queueRepository.deleteExpiredQueues();

        logger.info("Delete expired queue scheduler completed. Deleted {} expired queues.", expiredQueues.size());
        return expiredQueues;
    }

    @Transactional
    @Scheduled(cron = "0 */1 * * * *", zone = "Asia/Seoul")
    public void execute() {

        logger.info("시작");

        enterProcessingScheduler();
        expiredConcertBookingScheduler();
        deleteQueueScheduler();

        logger.info("끝");
    }
}
