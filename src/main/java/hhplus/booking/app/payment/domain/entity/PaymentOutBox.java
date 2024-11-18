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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payment_outbox")
public class PaymentOutBox extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentOutBoxId;

    private Long concertBookingId;

    private Long price;

    private String status;

    @PrePersist
    public void prePersist() {
        this.status = this.status == null ? "CREATED" : this.status;
    }

    public static PaymentOutBox of(
            Long concertBookingId,
            Long price
    ) {
        return PaymentOutBox.builder()
                .concertBookingId(concertBookingId)
                .price(price)
                .build();
    }



    @Builder
    public PaymentOutBox(
            Long paymentOutBoxId,
            Long concertBookingId,
            Long price,
            String status
    ) {
        this.paymentOutBoxId = paymentOutBoxId;
        this.concertBookingId = concertBookingId;
        this.price = price;
        this.status = status;
    }
}
