package hhplus.booking.app.user.application;

import hhplus.booking.config.security.BcryptEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class QueueServiceTest {

    @Spy
    private BcryptEncryptor bcryptEncryptor; // Spring 컨텍스트에서 주입

    @InjectMocks
    UserService userService;

    @Test
    void getUserToken() {

        String userTokenValue = UUID.randomUUID().toString();
        String tokenValue = bcryptEncryptor.encrypt(userTokenValue);
        System.out.println("UUID: " + userTokenValue +  " UserToken: " + tokenValue);

        boolean isMatch = bcryptEncryptor.isMatch(tokenValue, "$2a$10$5XsjWS/qMPzdlZFAxetlVucaF6HFCRhp/U.4I1tqhNRP7NDIp.eYa");

        System.out.println(isMatch);

    }
}