package hhplus.booking.app.queue.application.dto;

import hhplus.booking.app.user.interfaces.api.dto.UserTokenInfo;

public class QueueInfo {
    public record Input(String authorizationHeader) {}
    public record Output(UserTokenInfo userTokenInfo, Long queueRank) {}
}
