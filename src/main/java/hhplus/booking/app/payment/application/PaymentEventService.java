package hhplus.booking.app.payment.application;

import com.slack.api.webhook.Payload;
import hhplus.booking.app.payment.domain.event.kafka.dto.PaymentSuccessEvent;
import hhplus.booking.app.payment.infra.slack.SlackMessageSender;
import hhplus.booking.app.payment.infra.slack.SlackMessageTemplate;
import hhplus.booking.config.exception.BusinessException;
import hhplus.booking.config.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentEventService {

    private final SlackMessageSender slackMessageSender;

    public void sendSlackMessage(PaymentSuccessEvent paymentSuccessEvent) {

        try {

            Payload payload = SlackMessageTemplate.paymentSuccessTemplate(paymentSuccessEvent);
            slackMessageSender.send(payload);
            log.info("======== 결과 슬랙 전송 완료 ========");

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SLACK_SEND_FAIL);
        }

    }
}
