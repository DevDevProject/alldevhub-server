package com.example.redis.popular;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.example.redis.RedisLockService;
import com.example.redis.record.RedisScoreRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final ExecutorService dbWriteExecutor;
    private final PopularCacheService cacheService;
    private final StringRedisTemplate redis;

    private static final int SHARD_COUNT = 3;
    private static final String ZSET_KEY_PREFIX = "popular:recruit:shard:";
    private static final int REDIS_BATCH_SIZE = 10_000;

    public void increaseScore(Long id) {
        int shard = Math.abs(id.hashCode() % SHARD_COUNT);
        String shardKey = ZSET_KEY_PREFIX + shard;
        cacheService.incrementScore(shardKey, id.toString(), 1);
    }

    public Set<String> getTopIds(int topN) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(SHARD_COUNT);
        Map<String, Double> globalScoreMap = new ConcurrentHashMap<>();
        List<String> shardKeys = new ArrayList<>();

        for (int i = 0; i < SHARD_COUNT; i++) {
            shardKeys.add(ZSET_KEY_PREFIX + i);
        }

        List<Future<?>> futures = new ArrayList<>();

        for (String shardKey : shardKeys) {
            futures.add(executor.submit(() -> {
                long totalSize = cacheService.count(shardKey);
                final int BATCH_SIZE = 1_000_000;

                for (int offset = 0; offset < totalSize; offset += BATCH_SIZE) {
                    Set<ZSetOperations.TypedTuple<String>> chunk =
                            cacheService.fetchRangeWithScores(shardKey, offset, offset + BATCH_SIZE - 1);

                    if (chunk != null) {
                        for (ZSetOperations.TypedTuple<String> tuple : chunk) {
                            String id = tuple.getValue();
                            Double score = tuple.getScore() != null ? tuple.getScore() : 0.0;
                            globalScoreMap.merge(id, score, Double::sum);
                        }
                    }
                }
            }));
        }

        for (Future<?> f : futures) f.get();
        executor.shutdown();

        return globalScoreMap.entrySet().stream()
                             .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                             .limit(topN)
                             .map(Map.Entry::getKey)
                             .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public void processZSetMembers(String shardKey, Consumer<List<RedisScoreRecord>> consumer) {
        ScanOptions options = ScanOptions.scanOptions().count(REDIS_BATCH_SIZE).build();
        Cursor<ZSetOperations.TypedTuple<String>> cursor = cacheService.scan(shardKey, options);

        List<RedisScoreRecord> batchList = new ArrayList<>();
        while (cursor.hasNext()) {
            ZSetOperations.TypedTuple<String> t = cursor.next();
            batchList.add(new RedisScoreRecord(
                    t.getValue(),
                    t.getScore() != null ? t.getScore() : 0.0
            ));

            if (batchList.size() >= REDIS_BATCH_SIZE) {
                consumer.accept(new ArrayList<>(batchList));
                batchList.clear();
            }
        }

        if (!batchList.isEmpty()) {
            consumer.accept(new ArrayList<>(batchList));
            batchList.clear();
        }
    }
}
