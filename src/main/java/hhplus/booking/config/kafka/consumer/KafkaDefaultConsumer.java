package hhplus.booking.config.kafka.consumer;

import hhplus.booking.app.payment.domain.PaymentEventListener;
import hhplus.booking.app.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaDefaultConsumer implements PaymentEventListener {

    private final PaymentRepository paymentRepository;

    @KafkaListener(topics = "testTopic", groupId = "group_1")
    public void listener(String data) {
        log.info("리슨~~~~카프카리슨~~~~ 내용: {}", data);
    }

    @KafkaListener(topics = "payment", groupId = "group_1")
    public void paymentSuccessHandler(String data) {
        log.info("--------");
        Long concertBookingId = Long.parseLong(data.split(",")[0]);
        Long paymentId = Long.parseLong(data.split(",")[1]);
        log.info("결제완료!!! 예약번호: {}, 결제번호: {}", concertBookingId, paymentId);
        log.info("========== 완료된 정보를 저장합니다.==========");
        // TODO paymentRepository Update -> kakao or slack 등 외부 전송
        log.info("========== 저장완료 ==========");
    }

    public void processingPayment() {

    }
}