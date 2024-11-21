package hhplus.booking.app.payment.domain.entity;

import hhplus.booking.app.common.converter.JsonMessageConverter;
import hhplus.booking.app.payment.application.dto.PaymentEventInfo;
import hhplus.booking.config.database.BaseTimeEntity;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "outbox")
public class OutBox extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outboxId;

    private String topic;

    private String messageKey;

    private String eventType;

    @Convert(converter = JsonMessageConverter.class)
    private PaymentEventInfo message;

    private LocalDateTime sentAt;

    private String status;

    @PrePersist
    public void prePersist() {
        this.status = this.status == null ? "CREATED" : this.status;
    }

    public static OutBox of(
            String topic,
            String messageKey,
            String eventType,
            PaymentEventInfo message
    ) {
        return OutBox.builder()
                .topic(topic)
                .messageKey(messageKey)
                .eventType(eventType)
                .message(message)
                .build();
    }

    @Builder
    public OutBox(
            String topic,
            String messageKey,
            String eventType,
            PaymentEventInfo message,
            LocalDateTime sentAt,
            String status
    ){
        this.topic = topic;
        this.messageKey = messageKey;
        this.eventType = eventType;
        this.message = message;
        this.sentAt = sentAt;
        this.status = status;
    }

    public void updateStatusPublished() {
        this.status = "PUBLISHED";
    }

    public void updateSentAt() {
        this.sentAt = LocalDateTime.now();
    }

    public void updateStatusCompleted() {
        this.status = "COMPLETED";
    }
}
