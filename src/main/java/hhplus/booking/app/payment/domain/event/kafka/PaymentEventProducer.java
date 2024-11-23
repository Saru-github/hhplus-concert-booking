package hhplus.booking.app.payment.domain.event.kafka;

import hhplus.booking.app.payment.domain.event.kafka.dto.PaymentSuccessEvent;

public interface PaymentEventProducer {

    void updateOutbox(PaymentSuccessEvent paymentSuccessEvent);
    void sendSuccessMessage(PaymentSuccessEvent paymentSuccessEvent);
}