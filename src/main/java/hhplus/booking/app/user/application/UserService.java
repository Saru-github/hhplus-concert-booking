package hhplus.booking.app.user.application;

import hhplus.booking.app.user.application.dto.UserPointInfo;
import hhplus.booking.app.user.domain.entity.User;
import hhplus.booking.app.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserPointInfo.Output getUserPoints(UserPointInfo.Input input) {

        User user = userRepository.getUser(input.userId());
        return new UserPointInfo.Output(user);
    }

    @Transactional
    public UserPointInfo.Output chargeUserPoints(UserPointInfo.Input input) {

        User user = userRepository.getUser(input.userId());
        user.chargePoints(input.amount());
        return new UserPointInfo.Output(user);
    }

    @Transactional
    public UserPointInfo.Output useUserPoints(UserPointInfo.Input input) {

        User user = userRepository.getUser(input.userId());
        user.usePoints(input.amount());
        return new UserPointInfo.Output(user);
    }
}
