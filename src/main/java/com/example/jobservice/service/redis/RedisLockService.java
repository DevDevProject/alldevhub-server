package com.example.jobservice.service.redis;

import java.time.Duration;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisLockService {
    private final StringRedisTemplate redis;

    /** @return 획득 성공 시 토큰(UUID), 실패 시 null */
    public String tryLock(String lockKey, Duration ttl) {
        String token = UUID.randomUUID().toString();
        Boolean ok = redis.opsForValue().setIfAbsent(lockKey, token, ttl);
        return Boolean.TRUE.equals(ok) ? token : null;
    }

    /** 토큰 비교 후 본인이 가진 락만 해제 */
    public void unlock(String lockKey, String token) {
        try {
            String current = redis.opsForValue().get(lockKey);
            if (token != null && token.equals(current)) {
                redis.delete(lockKey);
            }
        } catch (Exception ignore) {}
    }
}
