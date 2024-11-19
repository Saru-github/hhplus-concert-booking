package hhplus.booking.app.payment.domain;

public interface PaymentEventPublisher {

    void createOutBox(PaymentSuccessEvent event);
    void success(PaymentSuccessEvent event);
}