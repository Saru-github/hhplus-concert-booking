package hhplus.booking.interfaces.user.dto;

public class UserQueueInfo {
    public record Input() {}
    public record Output(Long userId, Long queueRank) {}
}
