package hhplus.booking.app.user.interfaces.api.dto;

public class UserQueueInfo {
    public record Input(String authorizationHeader, Long userId) {}
    public record Output(UserTokenInfo userTokenInfo, Long queueRank) {}
}
