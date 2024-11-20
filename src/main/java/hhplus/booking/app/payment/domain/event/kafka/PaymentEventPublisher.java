package hhplus.booking.app.payment.domain.event.kafka;

import hhplus.booking.app.payment.domain.event.kafka.dto.KafkaPaymentSuccessEvent;

public interface PaymentEventPublisher {

    void success(KafkaPaymentSuccessEvent kafkaPaymentSuccessEvent);
}