package hhplus.booking.app.concert.domain.entity;

import hhplus.booking.config.database.BaseTimeEntity;
import hhplus.booking.config.exception.BusinessException;
import hhplus.booking.config.exception.ErrorCode;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    private String status;

    private LocalDateTime expiredAt;

    @PrePersist
    public void prePersist() {
        this.status = this.status == null ? "BOOKED" : this.status;
        this.expiredAt = this.expiredAt == null ? LocalDateTime.now().plusMinutes(5) : this.expiredAt;
    }

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
            Long concertSeatId,
            String status,
            LocalDateTime expiredAt
    ) {
        this.concertBookingId = concertBookingId;
        this.userId = userId;
        this.concertSeatId = concertSeatId;
        this.status = status;
        this.expiredAt = expiredAt;
    }

    public void validConcertBookingStatus() {
        if (!"BOOKED".equals(this.status)) {
            throw new BusinessException(ErrorCode.PAYMENT_NOT_ALLOWED);
        }
    }

    public void expiredBookingStatus() {
        if("BOOKED".equals(this.status)) {
            this.status = "EXPIRED";
        }
    }

    public void updateBookingStatusToCompleted() {
        this.status = "COMPLETED";
        this.expiredAt = LocalDateTime.now();
    }
}
