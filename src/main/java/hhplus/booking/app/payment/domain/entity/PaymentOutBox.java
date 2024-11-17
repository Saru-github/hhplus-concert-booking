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
@Table(name = "payment_outbox")
public class PaymentOutBox extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentOutBoxId;

    private Long concertBookingId;

    public static PaymentOutBox of(
            Long concertBookingId
    ) {
        return PaymentOutBox.builder()
                .concertBookingId(concertBookingId)
                .build();
    }

    @Builder
    public PaymentOutBox(
            Long paymentOutBoxId,
            Long concertBookingId
    ) {
        this.paymentOutBoxId = paymentOutBoxId;
        this.concertBookingId = concertBookingId;
    }
}
