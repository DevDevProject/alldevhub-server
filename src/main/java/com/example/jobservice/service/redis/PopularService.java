package com.example.jobservice.service.redis;

import com.example.jobservice.mapper.recruit.JobRecruitPopularMapper;
import com.example.jobservice.mapper.recruit.dto.JobRecruitPopular;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PopularService {

    private final StringRedisTemplate redis;
    private final RedisLockService redisLock;
    private final JobRecruitPopularMapper jobRecruitPopularMapper;

    @Qualifier("objectRedisTemplate")
    private final RedisTemplate<String, Object> objectRedisTemplate;

    private static final String HASH_TAG = "{pop}";
    private static final String ZSET_KEY_PREFIX = "popular:recruit:{pop}:shard:";
    private static final String GLOBAL_UNION_KEY = "popular:recruit:{pop}:union";
    private static final String CACHE_KEY_PREFIX = "popular:recruit:{pop}:top:r";
    private static final String REFRESH_LOCK_KEY = "lock:popular:recruit:{pop}:refresh";

    private static final int TOP_N = 10;
    private static final int SHARD_COUNT = 4;
    private static final int CACHE_REPLICAS = 3;

    private static final Duration ZSET_TTL = Duration.ofHours(24);
    private static final Duration CACHE_TTL = Duration.ofHours(1);
    private static final Duration LOCK_TTL = Duration.ofSeconds(15);

    public void increaseRecruitScore(Long recruitId) {
        int shard = Math.abs(recruitId.hashCode()) % SHARD_COUNT;
        String shardKey = ZSET_KEY_PREFIX + shard;
        redis.opsForZSet().incrementScore(shardKey, recruitId.toString(), 1);
        redis.expire(shardKey, ZSET_TTL);
    }

    public List<JobRecruitPopular> getPopularRecruits() {
        String key = pickReplicaKey();
        Object val = objectRedisTemplate.opsForValue().get(key);

        if (val != null)
            return castList(val);

        for (int i = 0; i < CACHE_REPLICAS; i++) {
            String alt = CACHE_KEY_PREFIX + i;
            if (alt.equals(key)) continue;
            Object altVal = objectRedisTemplate.opsForValue().get(alt);

            if (altVal != null)
                return castList(altVal);
        }

        String token = redisLock.tryLock(REFRESH_LOCK_KEY, LOCK_TTL);
        if (token != null) {
            try {
                refreshPopularRecruitsCacheInternal();
            } finally {
                redisLock.unlock(REFRESH_LOCK_KEY, token);
            }
            Object after = objectRedisTemplate.opsForValue().get(key);
            if (after != null)
                return castList(after);
        }
        return Collections.emptyList();
    }

    public void refreshPopularRecruitsCache() {
        String token = redisLock.tryLock(REFRESH_LOCK_KEY, LOCK_TTL);
        if (token == null)
            return;

        try {
            refreshPopularRecruitsCacheInternal();
        } finally {
            redisLock.unlock(REFRESH_LOCK_KEY, token);
        }
    }

    private void refreshPopularRecruitsCacheInternal() {
        List<String> shardKeys = new ArrayList<>(SHARD_COUNT);
        for (int i = 0; i < SHARD_COUNT; i++)  {
            shardKeys.add(ZSET_KEY_PREFIX + i);
        }

        unionShardsIntoGlobal(shardKeys);

        Set<String> topIdStrings = redis.opsForZSet().reverseRange(GLOBAL_UNION_KEY, 0, TOP_N - 1);
        if (topIdStrings == null || topIdStrings.isEmpty()) {
            writeReplicas(Collections.emptyList());
            return;
        }

        List<Long> idList = topIdStrings.stream().map(Long::valueOf).collect(Collectors.toList());
        List<JobRecruitPopular> rows = jobRecruitPopularMapper.findPopularRecruits(idList);
        Map<Long, JobRecruitPopular> byId = rows.stream()
                                                .collect(Collectors.toMap(JobRecruitPopular::getId, x -> x));
        List<JobRecruitPopular> ordered = topIdStrings.stream()
                                                      .map(Long::valueOf)
                                                      .map(byId::get)
                                                      .filter(Objects::nonNull)
                                                      .collect(Collectors.toList());

        writeReplicas(ordered);
    }

    private void unionShardsIntoGlobal(List<String> shards) {
        redis.execute((RedisCallback<Void>) connection -> {
            byte[] dest = PopularService.GLOBAL_UNION_KEY.getBytes(StandardCharsets.UTF_8);
            byte[][] keys = shards.stream()
                                  .map(k -> k.getBytes(StandardCharsets.UTF_8))
                                  .toArray(byte[][]::new);
            connection.zUnionStore(dest, keys);

            connection.expire(dest, 24 * 60 * 60);

            return null;
        });
    }

    private void writeReplicas(List<JobRecruitPopular> data) {
        for (int r = 0; r < CACHE_REPLICAS; r++) {
            String k = CACHE_KEY_PREFIX + r;
            objectRedisTemplate.opsForValue().set(k, data, CACHE_TTL);
        }
    }

    private String pickReplicaKey() {
        int r = ThreadLocalRandom.current().nextInt(CACHE_REPLICAS);
        return CACHE_KEY_PREFIX + r;
    }

    @SuppressWarnings("unchecked")
    private List<JobRecruitPopular> castList(Object o) {
        if (o instanceof List<?> list) {
            ObjectMapper mapper = new ObjectMapper();
            return list.stream()
                       .map(item -> {
                           if (item instanceof JobRecruitPopular jobRecruitPopular)
                               return jobRecruitPopular;
                           else
                               return mapper.convertValue(item, JobRecruitPopular.class);

                       })
                       .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
