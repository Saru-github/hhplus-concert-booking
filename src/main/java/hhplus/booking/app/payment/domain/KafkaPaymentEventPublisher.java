package hhplus.booking.app.payment.domain;

import hhplus.booking.app.payment.domain.repository.PaymentRepository;
import hhplus.booking.config.kafka.producer.KafkaDefaultProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;
import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaPaymentEventPublisher implements PaymentEventPublisher {

    private final PaymentRepository paymentRepository;

    private final KafkaDefaultProducer kafkaDefaultProducer;

    @Override
    @TransactionalEventListener(phase = BEFORE_COMMIT)
    public void createOutBox(PaymentSuccessEvent event) {
        paymentRepository.savePaymentOutBox(event.concertBookingId(), event.price());
        log.info("OutBox 생성완료!!! 콘서트 고유번호: {}, 가격: {}", event.concertBookingId(), event.price());
    }

    @Override
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void success(PaymentSuccessEvent event) {
        String message = event.price().toString() + "," + event.concertBookingId().toString();
        log.info("카프카 전송 메시지: {}", message);
        kafkaDefaultProducer.sendMessage(event.topic(), message);
    }

}
