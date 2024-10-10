package hhplus.booking.interfaces.user.dto;

public class UserTokenInfo {
    public record Input(Long userId) {}
    public record Output(String tokenValue) {}
}
