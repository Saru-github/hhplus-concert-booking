package hhplus.booking.config.kafka.producer;

import hhplus.booking.app.payment.application.dto.PaymentEventInfo;
import hhplus.booking.app.payment.domain.event.kafka.PaymentEventPublisher;
import hhplus.booking.app.payment.domain.event.kafka.dto.PaymentSuccessEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Duration;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Slf4j
@SpringBootTest
public class KafkaProduceTest {

    @Autowired
    private PaymentEventPublisher paymentEventPublisher;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private String receivedMessage;

    @Test
    @DisplayName("[성공] 카프카 연동 테스트, 브로커에 test-topic 토픽으로 test-message를 전송하고 최대 10초 대기하면서 listener가 받는 메시지와 일치한지 확인한다.")
    void produceTestMessage() {

        String message = "1,1";
        String topic = "payment";

        PaymentEventInfo paymentEventInfo = PaymentEventInfo.builder()
                .userName("대영")
                 .concertName("IU 콘서트")
                 .concertDate(LocalDate.now())
                 .seatNumber(11L)
                 .price(50000L)
                 .concertBookingId(1L)
                 .paymentId(1L)
                 .build();

        paymentEventPublisher.success(PaymentSuccessEvent.builder()
                .topic(topic)
                 .messageKey(null)
                 .eventType("test")
                 .message(paymentEventInfo)
                 .build());


        // paymentEventPublisher.success(new KafkaPaymentSuccessEvent( 1L, 1L));
        log.info("토픽 : {} , 보낸 메시지 : {}", topic, message);
        await()
        .atMost(Duration.ofSeconds(10))
        .untilAsserted(() -> {
            log.info("받은 메시지 : {}", receivedMessage);
            assertThat(receivedMessage).isEqualTo(message);
        });
    }

    @Test
    @DisplayName("[성공] 카프카 연동 테스트, 카프카 서버에 1000건 전송하고 최대 10초 대기하면서 listener가 받는 메시지와 일치한지 확인한다.")
    void ConCurrentProduceTestMessage() {

        String topic = "payment_test";

        // Given
        AtomicLong atomicExceptionCount = new AtomicLong(0L);

        // When
        for (int i = 0; i < 1000; i++) {
            Long finalI = (long) i;
            CompletableFuture.runAsync(() -> {
                try {
                    PaymentEventInfo paymentEventInfo = PaymentEventInfo.builder()
                                                                  .userName("대영")
                                                                 .concertName("IU 콘서트")
                                                                 .concertDate(LocalDate.now())
                                                                 .seatNumber(11L)
                                                                 .price(50000L)
                                                                 .concertBookingId(1L + finalI)
                                                                 .paymentId(1L + finalI)
                                                                 .build();


                     paymentEventPublisher.success(PaymentSuccessEvent.builder()
                                     .topic(topic)
                                     .messageKey(null)
                                     .eventType("test")
                                     .message(paymentEventInfo)
                                     .build());

                } catch (Exception e) {
                    atomicExceptionCount.getAndIncrement();
                }
            });
        }

        await()
        .atMost(Duration.ofSeconds(1000))
        .untilAsserted(() -> {
            log.info("받은 메시지 : {}", receivedMessage);
        });
    }

    @KafkaListener(topics = "payment_test", groupId = "payment_group")
    public void consumeTestMessage(String message) {
        this.receivedMessage = message;
    }

}