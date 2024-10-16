package hhplus.booking.app.concert.domain.entity;

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
@Table(name = "concert_booking")
public class ConcertBooking extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long concertBookingId;

    private Long userId;

    private Long concertSeatId;

    public static ConcertBooking of(
            Long userId,
            Long concertSeatId
    ) {
        return ConcertBooking.builder()
                .userId(userId)
                .concertSeatId(concertSeatId)
                .build();
    }

    @Builder
    public ConcertBooking(
            Long concertBookingId,
            Long userId,
            Long concertSeatId
    ) {
        this.concertBookingId = concertBookingId;
        this.userId = userId;
        this.concertSeatId = concertSeatId;
    }
}
