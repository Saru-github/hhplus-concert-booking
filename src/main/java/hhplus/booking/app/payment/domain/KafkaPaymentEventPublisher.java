package hhplus.booking.app.payment.domain;

import hhplus.booking.config.kafka.producer.KafkaDefaultProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaPaymentEventPublisher implements PaymentEventPublisher {

    private final KafkaDefaultProducer kafkaDefaultProducer;

    @Override
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void success(PaymentSuccessEvent event) {
        String message = event.paymentId().toString() + "," + event.concertBookingId().toString();
        log.info("카프카 전송 메시지: {}", message);
        kafkaDefaultProducer.sendMessage(event.topic(), message);
    }

}
