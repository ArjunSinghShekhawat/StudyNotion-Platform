package in.study_notion.utils;
import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.util.Base64;

@UtilityClass
public class GenerateToken {

    public static String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] tokenBytes = new byte[32];
        random.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }
}
