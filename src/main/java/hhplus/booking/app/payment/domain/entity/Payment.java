package hhplus.booking.app.payment.domain.entity;

import hhplus.booking.config.database.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payment")
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private Long concertBookingId;

    private Long paymentAmount;

    public static Payment of(
            Long concertBookingId,
            Long paymentAmount
    ) {
        return Payment.builder()
                .concertBookingId(concertBookingId)
                .paymentAmount(paymentAmount)
                .build();
    }

    @Builder
    public Payment(
            Long paymentId,
            Long concertBookingId,
            Long paymentAmount
    ) {
        this.paymentId = paymentId;
        this.concertBookingId = concertBookingId;
        this.paymentAmount = paymentAmount;
    }
}
