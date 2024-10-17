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
@Table(name = "payment")
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private Long userId;

    private Long concertSeatId;

    private Long paymentAmount;


    public static Payment of(
            Long userId,
            Long concertSeatId,
            Long paymentAmount
    ) {
        return Payment.builder()
                .userId(userId)
                .concertSeatId(concertSeatId)
                .paymentAmount(paymentAmount)
                .build();
    }

    @Builder
    public Payment(
            Long userId,
            Long concertSeatId,
            Long paymentAmount
    ) {
        this.userId = userId;
        this.concertSeatId = concertSeatId;
        this.paymentAmount = paymentAmount;
    }
}
