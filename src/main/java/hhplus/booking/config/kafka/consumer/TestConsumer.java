package hhplus.booking.config.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestConsumer {
 
    @KafkaListener(topics = "testTopic", groupId = "group_1")
    public void listener(String data) {
        log.info("리슨~~~~카프카리슨~~~~ 내용: {}", data);
    }
}