package hhplus.booking.app.user.domain.entity;

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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "\"user\"")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;

    private Long balance;

    @Version
    private Long version;

    @PrePersist
    public void prePersist() {
        this.balance = this.balance == null ? 0L : this.balance;
    }

    public static User of(String userName) {
        return User.builder()
                .userName(userName)
                .build();
    }

    @Builder
    public User(Long userId, String userName, Long balance) {
        this.userId = userId;
        this.userName = userName;
        this.balance = balance;
    }

    public void chargePoints(Long amount) {
        this.balance += amount;
    }

    public void usePoints(Long amount) {
        long remainingBalance = this.balance - amount;

        if (0 > remainingBalance) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_USER_POINTS);
        }

        this.balance = remainingBalance;
    }
}
