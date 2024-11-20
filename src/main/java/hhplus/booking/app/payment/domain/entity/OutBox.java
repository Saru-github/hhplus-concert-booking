package hhplus.booking.app.payment.domain.entity;

import hhplus.booking.config.database.BaseTimeEntity;
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

    private String messageKey;
    private String domainType;
    private String eventType;
    private String message;
    private LocalDateTime sentAt;
    private String status;

    @PrePersist
    public void prePersist() {
        this.status = this.status == null ? "CREATED" : this.status;
    }

    public static OutBox of(
            String domainType,
            String eventType,
            String message
    ) {
        return OutBox.builder()
                .domainType(domainType)
                .eventType(eventType)
                .message(message)
                .build();
    }

    @Builder
    public OutBox(
            String messageKey,
            String domainType,
            String eventType,
            String message,
            LocalDateTime sentAt,
            String status
    ){
        this.messageKey = messageKey;
        this.domainType = domainType;
        this.eventType = eventType;
        this.message = message;
        this.sentAt = sentAt;
        this.status = status;
    }
}
