package hhplus.booking.app.payment.infra.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaDefaultProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String sendMessage(String topic, Object object) {
        String jsonMessage = "";
        try {
            jsonMessage = objectMapper.writeValueAsString(object);
            kafkaTemplate.send(topic, jsonMessage);
        } catch (Exception e) {
            e.getStackTrace();
        }
        return jsonMessage;
    }
}
