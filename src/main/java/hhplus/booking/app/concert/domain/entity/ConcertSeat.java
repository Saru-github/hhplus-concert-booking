package hhplus.booking.app.concert.domain.entity;

import hhplus.booking.config.database.BaseTimeEntity;
import hhplus.booking.config.exception.BusinessException;
import hhplus.booking.config.exception.ErrorCode;
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
@Table(name = "concert_seat")
public class ConcertSeat extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long concertSeatId;

    private Long concertScheduleId;

    private Long seatNumber;

    private Long price;

    private String status;

    public static ConcertSeat of(Long concertScheduleId, Long seatNumber, Long price, String status) {
        return ConcertSeat.builder()
                .concertScheduleId(concertScheduleId)
                .seatNumber(seatNumber)
                .price(price)
                .status(status)
                .build();
    }

    @Builder
    public ConcertSeat(Long concertSeatId, Long concertScheduleId, Long seatNumber, Long price, String status) {
        this.concertSeatId = concertSeatId;
        this.concertScheduleId = concertScheduleId;
        this.seatNumber = seatNumber;
        this.price = price;
        this.status = status;
    }

    public void validAvailableSeat() {

        if ("BOOKED".equals(this.status)) {
            throw new BusinessException(ErrorCode.SEAT_ALREADY_BOOKED);
        }

        if (!"AVAILABLE".equals(this.status)) {
            throw new BusinessException(ErrorCode.SEAT_UNAVAILABLE);
        }
    }

    public void updateSeatStatusToBooked() {
        if ("AVAILABLE".equals(this.status)) {
            this.status = "BOOKED";
        }
    }

    public void updateSeatStatusToAvailable() {
        if ("BOOKED".equals(this.status)) {
            this.status = "AVAILABLE";
        }

    }
}
