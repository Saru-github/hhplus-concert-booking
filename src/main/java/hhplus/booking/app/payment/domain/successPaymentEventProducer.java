package hhplus.booking.app.payment.domain;

import hhplus.booking.app.payment.domain.entity.OutBox;
import hhplus.booking.app.payment.domain.event.kafka.PaymentEventProducer;
import hhplus.booking.app.payment.domain.event.kafka.dto.PaymentSuccessEvent;
import hhplus.booking.app.payment.infra.jpa.OutBoxJpaRepository;
import hhplus.booking.app.payment.infra.producer.KafkaDefaultProducer;
import hhplus.booking.app.queue.domain.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;
import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

@Slf4j
@Component
@RequiredArgsConstructor
public class successPaymentEventProducer implements PaymentEventProducer {

    private final KafkaDefaultProducer kafkaDefaultProducer;
    private final QueueRepository queueRepository;
    private final OutBoxJpaRepository outBoxJpaRepository;


    @Override
    @TransactionalEventListener(phase = BEFORE_COMMIT)
    public void updateOutbox(PaymentSuccessEvent paymentSuccessEvent) {
        OutBox outBox = outBoxJpaRepository.findById(paymentSuccessEvent.outboxId())
                        .orElseThrow(() -> new IllegalStateException("outBoxId를 찾을 수 없습니다."));

        outBox.published();
        outBoxJpaRepository.save(outBox);
        log.info("======== OUTBOX PUBLISED 상태변경 완료 ========");
    }


    @Override
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void sendSuccessMessage(PaymentSuccessEvent paymentSuccessEvent) {

        queueRepository.deleteProcessingToken(paymentSuccessEvent.messageKey());
        log.info("======== 대기열 토큰: {} 삭제 완료 ========", paymentSuccessEvent.messageKey());

        String message = kafkaDefaultProducer.sendMessage("payment", paymentSuccessEvent);
        log.info("카프카 전송 메시지: {}", message);
    }
}
