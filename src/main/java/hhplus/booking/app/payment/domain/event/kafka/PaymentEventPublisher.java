package hhplus.booking.app.payment.domain.event.kafka;

import hhplus.booking.app.payment.domain.event.kafka.dto.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void success(PaymentSuccessEvent paymentSuccessEvent) {
        applicationEventPublisher.publishEvent(paymentSuccessEvent);
    }
}