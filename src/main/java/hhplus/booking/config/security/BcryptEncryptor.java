package hhplus.booking.config.security;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class BcryptEncryptor {

    public String encrypt(String stringUUID) {
        return BCrypt.hashpw(stringUUID, BCrypt.gensalt());
    }

    // 원본 비밀번호와 해시된 비밀번호가 일치하는지 확인하는 메서드
    public boolean isMatch(String origin, String hashed) {
        try {
            return BCrypt.checkpw(origin, hashed);
        } catch (Exception e) {
            return false;
        }
    }
}
