package hhplus.booking.app.user.application;

import hhplus.booking.app.user.domain.entity.User;
import hhplus.booking.app.user.domain.repository.UserRepository;
import hhplus.booking.app.user.interfaces.api.dto.UserQueueInfo;
import hhplus.booking.config.security.BcryptEncryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final BcryptEncryptor bcryptEncryptor;
    private final UserRepository userRepository;

    public UserQueueInfo.Output generateToken(UserQueueInfo.Input input) {

        String tokenValue = bcryptEncryptor.encrypt(UUID.randomUUID().toString());
        return new UserQueueInfo.Output();
    }

    public UserQueueInfo.Output getUserQueueRank(UserQueueInfo.Input input) {

        // TODO: userId 값 부터 검증해야 함.
        User user = userRepository.getUser();

        // TODO: userId 로 토큰 및 상태조회
        // TODO: 토큰 조회 X = 토큰 발급 후 대기열 등록
        // TODO: 상태 Waiting -> 대기열 조회
        // TODO: 상태 Processing -> 입장
        Token token = userRepoitory.getToken(input);

        if (String.valueOf(token == null) {
            token = generateToken(input);
        }

        bcryptEncryptor.isMatch(userTokenValue, input.userTokenInfo());
    }
}
