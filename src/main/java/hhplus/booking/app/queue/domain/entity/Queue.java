package hhplus.booking.app.queue.domain.entity;

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
@Table(name = "\"user\"")
public class Queue extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long queueId;

    private String userName;


    public static Queue of(Long userId, String userName) {
        return Queue.builder()
                .userId(userId)
                .userName(userName)
                .build();
    }

    @Builder
    public Queue(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }
}
