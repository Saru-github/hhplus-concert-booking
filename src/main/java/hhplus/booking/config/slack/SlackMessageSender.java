package hhplus.booking.config.slack;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class SlackMessageSender implements DisposableBean {

    private final Slack slack = Slack.getInstance();

    public void send(Payload payload) {
        try {
            slack.send("https://hooks.slack.com/services/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", payload);
        } catch (IOException ie) {
            log.error("슬랙 메시지 발송 오류", ie);
        }
    }

    @Override
    public void destroy() throws Exception {
        slack.close();
    }

}