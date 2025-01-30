package net.azelit.url.util;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@Component
public class UrlShortenerUtil {
    public static String generateHash(String originalUrl)
            throws NoSuchAlgorithmException {
        String uuid = UUID.randomUUID().toString();
        String toHash = originalUrl + uuid;

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(toHash.getBytes(StandardCharsets.UTF_8));

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(hashBytes)
                .substring(0, 8);
    }
}
