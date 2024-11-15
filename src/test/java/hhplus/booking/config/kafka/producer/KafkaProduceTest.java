package hhplus.booking.config.kafka.producer;

import hhplus.booking.config.kafka.consumer.TestConsumer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class KafkaProduceTest {

    @Autowired
    private TestProducer testProducer;

    @Autowired
    private TestConsumer testConsumer;

    @Test
    @DisplayName("카프카 이벤트 발행 테스트")
    void testProduce() throws InterruptedException {

        // given
        String message = "보낼 메시지 내용내용내용내용내용";

        // when
        testProducer.create(message);
//        Thread.sleep(2000);
//        log.info("카프카프카프카프카프카프카프카 토픽생성");
//        testConsumer.listener(message);
        // then

    }

}