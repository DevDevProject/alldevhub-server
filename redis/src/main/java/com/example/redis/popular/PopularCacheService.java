package com.example.redis.popular;

import com.example.redis.RedisLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public class PopularCacheService {

    private final StringRedisTemplate redis;
    private final RedisLockService redisLock;
    private final RedisTemplate<String, Object> objectRedisTemplate;

    private static final String HASH_TAG = "{pop}";
    private static final String ZSET_KEY_PREFIX = "popular:recruit:{pop}:shard:";
    private static final String GLOBAL_UNION_KEY = "popular:recruit:{pop}:union";
    private static final String CACHE_KEY_PREFIX = "popular:recruit:{pop}:top:r";
    private static final String REFRESH_LOCK_KEY = "lock:popular:recruit:{pop}:refresh";

    private static final int SHARD_COUNT = 4;
    private static final int CACHE_REPLICAS = 3;

    private static final Duration ZSET_TTL = Duration.ofHours(24);
    private static final Duration CACHE_TTL = Duration.ofHours(1);
    private static final Duration LOCK_TTL = Duration.ofSeconds(15);

    /** 점수 증가 */
    public void increaseScore(Long id) {
        int shard = Math.abs(id.hashCode()) % SHARD_COUNT;
        String shardKey = ZSET_KEY_PREFIX + shard;
        redis.opsForZSet().incrementScore(shardKey, id.toString(), 1);
        redis.expire(shardKey, ZSET_TTL);
    }

    /** 캐시 읽기 */
    @SuppressWarnings("unchecked")
    public <T> List<T> readCache(Class<T> clazz) {
        String key = pickReplicaKey();
        Object val = objectRedisTemplate.opsForValue().get(key);
        if (val instanceof List<?> list) {
            return (List<T>) list;
        }
        return Collections.emptyList();
    }

    /** 캐시 쓰기 */
    public <T> void writeCache(List<T> data) {
        for (int r = 0; r < CACHE_REPLICAS; r++) {
            String k = CACHE_KEY_PREFIX + r;
            objectRedisTemplate.opsForValue().set(k, data, CACHE_TTL);
        }
    }

    /** 상위 id 추출 */
    public Set<String> getTopIds(int topN) {
        List<String> shardKeys = new ArrayList<>(SHARD_COUNT);
        for (int i = 0; i < SHARD_COUNT; i++) {
            shardKeys.add(ZSET_KEY_PREFIX + i);
        }
        unionShardsIntoGlobal(shardKeys);
        return redis.opsForZSet().reverseRange(GLOBAL_UNION_KEY, 0, topN - 1);
    }

    /** 락 */
    public String tryLock() {
        return redisLock.tryLock(REFRESH_LOCK_KEY, LOCK_TTL);
    }

    public void unlock(String token) {
        redisLock.unlock(REFRESH_LOCK_KEY, token);
    }

    private void unionShardsIntoGlobal(List<String> shards) {
        redis.execute((RedisCallback<Void>) connection -> {
            byte[] dest = GLOBAL_UNION_KEY.getBytes(StandardCharsets.UTF_8);
            byte[][] keys = shards.stream().map(k -> k.getBytes(StandardCharsets.UTF_8)).toArray(byte[][]::new);
            connection.zUnionStore(dest, keys);
            connection.expire(dest, (int) ZSET_TTL.toSeconds());
            return null;
        });
    }

    private String pickReplicaKey() {
        int r = ThreadLocalRandom.current().nextInt(CACHE_REPLICAS);
        return CACHE_KEY_PREFIX + r;
    }
}
