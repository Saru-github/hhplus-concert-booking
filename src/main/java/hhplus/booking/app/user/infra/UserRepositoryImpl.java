package hhplus.booking.app.user.infra;

import hhplus.booking.app.user.domain.entity.User;
import hhplus.booking.app.user.domain.repository.UserRepository;
import hhplus.booking.app.user.infra.jpa.UserJpaRepository;
import hhplus.booking.config.exception.BusinessException;
import hhplus.booking.config.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User getUser(Long userId) {
        return userJpaRepository.findById(userId).orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public User findUserWithLockById(Long userId) {
        return userJpaRepository.findUserWithLockById(userId).orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}
