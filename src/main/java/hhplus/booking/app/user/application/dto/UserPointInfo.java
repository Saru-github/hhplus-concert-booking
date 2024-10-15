package hhplus.booking.app.user.application.dto;

import hhplus.booking.app.user.domain.entity.User;
import lombok.Builder;

public class UserPointInfo {
    public record Input(Long userId, Long amount) {}
    @Builder
    public record Output(
            Long userId,
            String userName,
            Long balance
    ) { public Output(User user) {
            this(
                user.getUserId(),
                user.getUserName(),
                user.getBalance()
            );
        }
    }
}
