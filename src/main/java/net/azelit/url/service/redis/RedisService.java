package net.azelit.url.service.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate stringRedisTemplate;

    public void cacheHash(String hash) {
        stringRedisTemplate.opsForValue().set(hash, hash);
    }

    public String getCachedUrl(String originalUrl) {
        return stringRedisTemplate.opsForValue().get("short:" + originalUrl);
    }

    public void cacheShortUrl(String originalUrl, String shortUrl) {
        stringRedisTemplate.opsForValue().set("short:" + originalUrl, shortUrl);
    }

    public boolean isHashCached(String hash) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(hash));
    }

    public void removeCache(String hash) {
        stringRedisTemplate.delete(hash);
    }
}
