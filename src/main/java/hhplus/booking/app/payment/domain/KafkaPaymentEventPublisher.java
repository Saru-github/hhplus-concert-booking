package hhplus.booking.app.payment.domain;

import hhplus.booking.app.payment.domain.event.kafka.PaymentEventPublisher;
import hhplus.booking.app.payment.domain.event.kafka.dto.KafkaPaymentSuccessEvent;
import hhplus.booking.app.payment.infra.producer.KafkaDefaultProducer;
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
    public void success(KafkaPaymentSuccessEvent kafkaPaymentSuccessEvent) {

        String message = kafkaDefaultProducer.sendMessage("payment", kafkaPaymentSuccessEvent);
        log.info("카프카 전송 메시지: {}", message);
    }

}
