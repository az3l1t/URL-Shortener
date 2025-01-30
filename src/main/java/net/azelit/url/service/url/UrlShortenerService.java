package net.azelit.url.service.url;

import lombok.RequiredArgsConstructor;
import net.azelit.url.entity.UrlMapping;
import net.azelit.url.exception.NoUniqueCacheException;
import net.azelit.url.repository.UrlMappingRepository;
import net.azelit.url.service.redis.RedisService;
import net.azelit.url.util.UrlShortenerUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {
    private final UrlMappingRepository urlMappingRepository;
    private final RedisService redisService;
    private static final int MAX_TRY = 10;

    @Transactional
    public String shortenUrl(String originalUrl)
            throws NoSuchAlgorithmException {
        String cachedShortUrl = redisService.getCachedUrl(originalUrl);
        if (cachedShortUrl != null) {
            return cachedShortUrl;
        }

        UrlMapping existingMapping = urlMappingRepository.findByOriginalUrl(originalUrl);
        if (existingMapping != null) {
            redisService.cacheShortUrl(originalUrl, existingMapping.getShortenedUrl());
            return existingMapping.getShortenedUrl();
        }

        String hash = generateUniqueHash(originalUrl);
        String shortenedUrl = "http://shorty/" + hash;

        UrlMapping newUrlMapping = UrlMapping.builder()
                .originalUrl(originalUrl)
                .shortenedUrl(shortenedUrl)
                .hash(hash)
                .expiredAt(LocalDateTime.now().plusDays(7))
                .build();
        urlMappingRepository.save(newUrlMapping);

        redisService.cacheShortUrl(originalUrl, shortenedUrl);
        redisService.cacheHash(hash);

        return shortenedUrl;
    }

    private String generateUniqueHash(String originalUrl) throws NoSuchAlgorithmException {
        for (int i = 0; i < MAX_TRY; i++) {
            String hash = UrlShortenerUtil.generateHash(originalUrl);
            if (!redisService.isHashCached(hash) && !urlMappingRepository.existsByHash(hash)) {
                return hash;
            }
        }
        throw new NoUniqueCacheException("Не удалось сгенерировать уникальный хэш за " + MAX_TRY + " попыток.");
    }
}
