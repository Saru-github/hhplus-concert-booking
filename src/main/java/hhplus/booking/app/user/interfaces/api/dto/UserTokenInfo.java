package hhplus.booking.app.user.interfaces.api.dto;

public record UserTokenInfo (
        String tokenValue,
        Long userId
){ }
