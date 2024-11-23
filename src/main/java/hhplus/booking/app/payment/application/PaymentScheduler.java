package hhplus.booking.app.payment.application;

import hhplus.booking.app.payment.domain.event.kafka.PaymentEventProducer;
import hhplus.booking.app.payment.domain.event.kafka.dto.PaymentSuccessEvent;
import hhplus.booking.app.payment.infra.jpa.OutBoxJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentScheduler {

    private PaymentEventProducer paymentEventProducer;
    private OutBoxJpaRepository outBoxJpaRepository;

    @Transactional
    @Scheduled(cron = "*/5 * * * * *", zone = "Asia/Seoul")
    public void execute() {
        log.info("======== OUTBOX 누락 데이터 스케쥴러 시작 ========");
        outBoxJpaRepository.findAllByStatus("CREATED").forEach(item -> {
        	if (item.getCreatedAt().isBefore(LocalDateTime.now().minusSeconds(5))) {
                paymentEventProducer.sendSuccessMessage(new PaymentSuccessEvent(item));
                log.info("======== OUTBOX 누락 데이터 outboxId:{}  전송완료 ========", item.getOutboxId());
        	}
        });
    }
}
