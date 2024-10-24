package hhplus.booking.app.user.application.dto;

import hhplus.booking.app.user.domain.entity.User;
import hhplus.booking.config.exception.BusinessException;
import hhplus.booking.config.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public class UserPointInfo {

    public record Input(Long userId,
                        Long amount
    ) {
        public Input {
            if (amount != null && 0 >= amount) {
                throw new BusinessException(ErrorCode.INVALID_POINT_REQUEST);
            }
        }
    }

    @Builder
    @Schema(name = "UserPointOutput")
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
