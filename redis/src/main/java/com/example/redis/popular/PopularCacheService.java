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

    private static final String GLOBAL_UNION_KEY = "popular:recruit:union";
    private static final String CACHE_KEY_PREFIX = "popular:recruit:top";

    private static final String ZSET_KEY = "popular:recruit";
    private static final String ZSET_KEY_PREFIX = "popular:recruit:shard:";
    private static final String REFRESH_LOCK_KEY = "lock:popular:recruit:refresh";

    private static final int CACHE_REPLICAS = 3;

    private static final int SHARD_COUNT = 3;

    private static final Duration ZSET_TTL = Duration.ofHours(24);
    private static final Duration CACHE_TTL = Duration.ofHours(1);
    private static final Duration LOCK_TTL = Duration.ofSeconds(15);

    public void increaseScore(Long id) {
        int shard = Math.abs(id.hashCode() % SHARD_COUNT);
        String shardKey = ZSET_KEY_PREFIX + shard;
        redis.opsForZSet().incrementScore(shardKey, id.toString(), 1);
        redis.expire(shardKey, ZSET_TTL);
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

    public Set<String> getTopIds(int topN) {
        List<String> shardKeys = new ArrayList<>();
        for (int i = 0; i < SHARD_COUNT; i++) {
            shardKeys.add(ZSET_KEY_PREFIX + i);
        }

        Map<String, Double> scoreMap = new HashMap<>();
        for (String shardKey : shardKeys) {
            Set<ZSetOperations.TypedTuple<String>> tuples = redis.opsForZSet().reverseRangeWithScores(shardKey, 0, -1);
            if (tuples != null) {
                for (ZSetOperations.TypedTuple<String> t : tuples) {
                    String id = t.getValue();
                    Double score = t.getScore() != null ? t.getScore() : 0.0;
                    scoreMap.put(id, scoreMap.getOrDefault(id, 0.0) + score);
                }
            }
        }


        return scoreMap.entrySet().stream()
                       .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                       .limit(topN)
                       .map(Map.Entry::getKey)
                       .collect(Collectors.toCollection(LinkedHashSet::new));
    }

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
