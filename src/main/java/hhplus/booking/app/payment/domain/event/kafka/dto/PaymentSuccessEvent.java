package hhplus.booking.app.payment.domain.event.kafka.dto;

import hhplus.booking.app.payment.application.dto.PaymentEventInfo;
import hhplus.booking.app.payment.domain.entity.OutBox;
import lombok.Builder;

@Builder
public record PaymentSuccessEvent(
        Long outboxId,
        String topic,
        String messageKey,
        String eventType,
        PaymentEventInfo message
) {
    public PaymentSuccessEvent(OutBox outBox) {
        this(
            outBox.getOutboxId(),
            outBox.getTopic(),
            outBox.getMessageKey(),
            outBox.getEventType(),
            outBox.getMessage()
        );
    }
}
