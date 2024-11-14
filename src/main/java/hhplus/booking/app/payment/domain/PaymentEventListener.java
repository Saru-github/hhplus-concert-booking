package hhplus.booking.app.payment.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void paymentSuccessHandler(PaymentSuccessEvent paymentsuccessevent) {
        log.info("결제완료!!! paymentId: {}, concertBookingId: {}", paymentsuccessevent.paymentId(), paymentsuccessevent.concertBookingId());
        log.info("========== 완료된 정보를 전달합니다.==========");
        log.info("========== 전달완료 ==========");
    }
}
