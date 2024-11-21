package hhplus.booking.app.payment.infra.slack;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class SlackMessageSender implements DisposableBean {

    @Value("${slack.webhookUrl}")
    private String slackWebhookUrl;

    private final Slack slack = Slack.getInstance();

    public void send(Payload payload) {
        try {
            slack.send(slackWebhookUrl, payload);
        } catch (IOException ie) {
            log.error("슬랙 메시지 발송 오류", ie);
        }
    }

    @Override
    public void destroy() throws Exception {
        slack.close();
    }

}