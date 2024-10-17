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

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "concert_schedule")
public class ConcertSchedule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long concertScheduleId;

    private Long concertId;

    private LocalDate concertDate;

    public static ConcertSchedule of(Long concertId, LocalDate concertDate) {
        return ConcertSchedule.builder()
                .concertId(concertId)
                .concertDate(concertDate)
                .build();
    }

    @Builder
    public ConcertSchedule(Long concertScheduleId, Long concertId, LocalDate concertDate) {
        this.concertScheduleId = concertScheduleId;
        this.concertId = concertId;
        this.concertDate = concertDate;
    }

}
