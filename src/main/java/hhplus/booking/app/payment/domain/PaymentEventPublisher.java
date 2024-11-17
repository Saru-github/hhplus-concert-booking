package hhplus.booking.app.payment.domain;

public interface PaymentEventPublisher {

    void success(PaymentSuccessEvent event);
}