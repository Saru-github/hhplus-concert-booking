package hhplus.booking.app.concert.domain.entity;

import hhplus.booking.app.queue.domain.entity.Queue;
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

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "concert")
public class Concert extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long concertId;

    private String concertName;

    public static Concert from(String concertName) {
        return Concert.builder()
                .concertName(concertName)
                .build();
    }

    @Builder
    public Concert(Long concertId, String concertName) {
        this.concertId = concertId;
        this.concertName = concertName;
    }


}
