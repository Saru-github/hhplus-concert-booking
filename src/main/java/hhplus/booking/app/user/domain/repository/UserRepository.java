package hhplus.booking.app.user.domain.repository;

import hhplus.booking.app.user.domain.entity.User;

public interface UserRepository {
    User getUser(Long userId);
}
