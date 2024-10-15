package hhplus.booking.app.queue.domain.entity;

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
@Table(name = "queue")
public class Queue extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long queueId;

    private String tokenValue;
    private String status;
    private LocalDateTime expiredAt;

    @PrePersist
    public void prePersist() {
        this.status = this.status == null ? "WAITING" : this.status;
        this.expiredAt = this.expiredAt == null ? LocalDateTime.now().plusMinutes(1) : this.expiredAt;
    }

    public static Queue from(String tokenValue) {
        return Queue.builder()
                .tokenValue(tokenValue)
                .build();
    }

    @Builder
    public Queue(String tokenValue, String status, LocalDateTime expiredAt) {
        this.tokenValue = tokenValue;
        this.status = status;
        this.expiredAt = expiredAt;
    }

    public Queue(Long queueId, String tokenValue, String status, LocalDateTime expiredAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.queueId = queueId;
        this.tokenValue = tokenValue;
        this.status = status;
        this.expiredAt = expiredAt;
    }

    public void refreshExpiration() {
        this.expiredAt = LocalDateTime.now();
    }
}
