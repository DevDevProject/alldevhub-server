package com.example.redis.popular;

import com.example.redis.RedisLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PopularCacheService {

    private final StringRedisTemplate redis;
    private final RedisLockService redisLock;
    private final RedisTemplate<String, Object> objectRedisTemplate;

    private static final String CACHE_KEY_PREFIX = "popular:recruit:top";

    private static final String REFRESH_LOCK_KEY = "lock:popular:recruit:refresh";


    private static final Duration ZSET_TTL = Duration.ofHours(24);
    private static final Duration CACHE_TTL = Duration.ofHours(1);
    private static final Duration LOCK_TTL = Duration.ofSeconds(15);

    public void incrementScore(String key, String member, double delta) {
        redis.opsForZSet().incrementScore(key, member, delta);
        redis.expire(key, ZSET_TTL);
    }

    public Set<ZSetOperations.TypedTuple<String>> fetchRangeWithScores(String key, long start, long end) {
        return redis.opsForZSet().reverseRangeWithScores(key, start, end);
    }

    public Long count(String key) {
        Long count = redis.opsForZSet().zCard(key);
        return count != null ? count : 0L;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> readCache(Class<T> clazz) {
        Object val = objectRedisTemplate.opsForValue().get(CACHE_KEY_PREFIX);
        if (val instanceof List<?> list) {
            return (List<T>) list;
        }
        return Collections.emptyList();
    }

    public <T> void writeCache(List<T> data) {
            objectRedisTemplate.opsForValue().set(CACHE_KEY_PREFIX, data, CACHE_TTL);
    }


    public String tryLock() {
        return redisLock.tryLock(REFRESH_LOCK_KEY, LOCK_TTL);
    }

    public void unlock(String token) {
        redisLock.unlock(REFRESH_LOCK_KEY, token);
    }
}
