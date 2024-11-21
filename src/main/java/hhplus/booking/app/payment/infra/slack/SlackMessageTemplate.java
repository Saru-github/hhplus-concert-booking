package hhplus.booking.app.payment.infra.slack;

import com.slack.api.webhook.Payload;
import hhplus.booking.app.payment.application.dto.PaymentEventInfo;
import hhplus.booking.app.payment.domain.event.kafka.dto.PaymentSuccessEvent;

import java.util.Collections;

import static com.slack.api.model.block.Blocks.asBlocks;
import static com.slack.api.model.block.Blocks.context;
import static com.slack.api.model.block.Blocks.divider;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;

public class SlackMessageTemplate {

    public static Payload paymentSuccessTemplate(PaymentSuccessEvent paymentSuccessEvent) {

    PaymentEventInfo paymentEventInfo = paymentSuccessEvent.message();

//        DateTimeFormatter originalFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//        DateTimeFormatter newFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        // startDate 변환
//        LocalDateTime startDateTime = LocalDateTime.parse(startDate, originalFormat);
//        String startTextToDate = startDateTime.format(newFormat);
//
//        // endDate 변환
//        LocalDateTime endDateTime = LocalDateTime.parse(endDate, originalFormat);
//        String endTextToDate = endDateTime.format(newFormat);

        return Payload.builder()
                .blocks(asBlocks(
                    context(c -> c.elements(Collections.singletonList(
                            markdownText("🎉 *`TEST-결제성공`* 🎉")
                    ))),
                    context(c -> c.elements(Collections.singletonList(
                            markdownText(
                                        "- 🙋 *예약자 성함*: " + paymentEventInfo.userName() + "님  \n" +
                                        "- 🎤 *콘서트 이름*: " + paymentEventInfo.concertName() + "  \n" +
                                        "- 📅 *콘서트 날짜*: " + paymentEventInfo.concertDate() + "  \n" +
                                        "- 💺 *콘서트 좌석*: " + paymentEventInfo.seatNumber() + "번 좌석  \n" +
                                        "- 💰 *티켓 가격*: " + paymentEventInfo.price() + " ₩  \n" +
                                        "- 🆔 *티켓 고유번호*: " + paymentEventInfo.concertBookingId() + "  \n" +
                                        "- 🧾 *결제 고유번호*: " + paymentEventInfo.paymentId() + "  \n" +
                                        "- ✔️ *상태*: 예매 완료! 🎵  \n\n" +
                                        "- ✨ *즐거운 관람 되세요!* 🍿🎶"
                                )
                            ))),
//                        context(c -> c.elements(Arrays.asList(
//                                markdownText("`시작 시간:` " + startDate + " (" + startTextToDate + ")"),
//                                markdownText("`종료 시간:` " + endDate + " (" + endTextToDate + ")")
                        divider()
                ))
                .build();
    }
}
