package hhplus.booking.app.payment.domain;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface PaymentEventListener {

    void paymentSuccessHandler(ConsumerRecord<String, String> record);
}
