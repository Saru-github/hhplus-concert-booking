package hhplus.booking.config.kafka;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Slf4j
@SpringBootTest
public class KafkaProduceTest {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private String receivedMessage;

    @Test
    @DisplayName("[성공] 카프카 연동 테스트, 브로커에 test-topic 토픽으로 test-message를 전송하고 최대 10초 대기하면서 listener가 받는 메시지와 일치한지 확인한다.")
    void produceTestMessage() {

        String message = "test-message";
        String topic = "test-topic";

        kafkaTemplate.send(topic, message);


        // paymentEventPublisher.success(new KafkaPaymentSuccessEvent( 1L, 1L));
        log.info("토픽 : {} , 보낸 메시지 : {}", topic, message);
        await()
        .atMost(Duration.ofSeconds(10))
        .untilAsserted(() -> {
            log.info("받은 메시지 : {}", receivedMessage);
            assertThat(receivedMessage).isEqualTo(message);
        });
    }

    @KafkaListener(topics = "test-topic", groupId = "group_1")
    public void consumeTestMessage(String message) {
        this.receivedMessage = message;
    }

}