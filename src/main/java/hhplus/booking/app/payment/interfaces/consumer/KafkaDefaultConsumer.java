package hhplus.booking.app.payment.interfaces.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slack.api.webhook.Payload;
import hhplus.booking.app.payment.domain.PaymentEventListener;
import hhplus.booking.app.payment.domain.event.kafka.dto.KafkaPaymentSuccessEvent;
import hhplus.booking.config.slack.SlackMessageSender;
import hhplus.booking.config.slack.SlackMessageTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaDefaultConsumer implements PaymentEventListener {

    private final SlackMessageSender slackMessageSender;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "testTopic", groupId = "group_1")
    public void listener(String data) {
        log.info("리슨~~~~카프카리슨~~~~ 내용: {}", data);
    }

    @KafkaListener(topics = "payment", groupId = "group_1")
    public void paymentSuccessHandler(ConsumerRecord<String, String> record) {
        String jsonMessage = record.value();

        try {
            KafkaPaymentSuccessEvent kafkaPaymentSuccessEvent = objectMapper.readValue(jsonMessage, KafkaPaymentSuccessEvent.class);
            Payload payload = SlackMessageTemplate.paymentSuccessTemplate(kafkaPaymentSuccessEvent);
            slackMessageSender.send(payload);
            log.info("결과 슬랙 전송 완료 ======");

        } catch (Exception e) {
            // 예외 처리 로직
            e.printStackTrace();
        }

    }

}