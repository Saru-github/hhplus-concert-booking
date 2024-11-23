package hhplus.booking.app.payment.interfaces.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hhplus.booking.app.payment.application.PaymentEventService;
import hhplus.booking.app.payment.domain.PaymentEventListener;
import hhplus.booking.app.payment.domain.entity.OutBox;
import hhplus.booking.app.payment.domain.event.kafka.dto.PaymentSuccessEvent;
import hhplus.booking.app.payment.infra.jpa.OutBoxJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaDefaultConsumer implements PaymentEventListener {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PaymentEventService paymentEventService;
    private final OutBoxJpaRepository outBoxJpaRepository;

    @KafkaListener(topics = "testTopic", groupId = "group_1")
    public void listener(String data) {
        log.info("리슨~~~~카프카리슨~~~~ 내용: {}", data);
    }

    @KafkaListener(topics = "payment", groupId = "group_1")
    public void paymentSuccessHandler(ConsumerRecord<String, String> record) {

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String jsonMessage = record.value();
        PaymentSuccessEvent paymentSuccessEvent;

        try {
            paymentSuccessEvent = objectMapper.readValue(jsonMessage, PaymentSuccessEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        paymentEventService.sendSlackMessage(paymentSuccessEvent);

        OutBox outBox = outBoxJpaRepository.findById(paymentSuccessEvent.outboxId())
                .orElseThrow(() -> new IllegalStateException("outBoxId를 찾을 수 없습니다."));

        outBox.updateStatusCompleted();
        outBoxJpaRepository.save(outBox);
        log.info("======== OUTBOX COMPLTED 상태변경 완료 ========");

    }

}