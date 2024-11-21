package hhplus.booking.app.payment.infra.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hhplus.booking.config.exception.BusinessException;
import hhplus.booking.config.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaDefaultProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String sendMessage(String topic, Object object) {
        String jsonMessage = "";
        try {
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            jsonMessage = objectMapper.writeValueAsString(object);
            kafkaTemplate.send(topic, jsonMessage);
            log.info("======== 카프카 전송 완료 ========");
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.KAFKA_SEND_FAIL);
        }

        return jsonMessage;
    }
}
