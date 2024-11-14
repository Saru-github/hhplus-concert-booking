package hhplus.booking.app.payment.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void success(PaymentSuccessEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
