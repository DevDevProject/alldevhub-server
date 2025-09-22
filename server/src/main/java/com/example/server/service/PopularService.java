package com.example.server.service;

import com.example.kafka.producer.PopularProducer;
import com.example.redis.popular.PopularCacheService;
import com.example.redis.popular.RankingService;
import com.example.server.mapper.recruit.JobRecruitPopularMapper;
import com.example.common.dto.common.JobRecruitDailyView;
import com.example.server.mapper.recruit.dto.JobRecruitPopular;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PopularService {

    private final PopularProducer producer;
    private final RankingService rankingService;
    private final PopularCacheService popularCache;
    private final JobRecruitPopularMapper jobRecruitPopularMapper;

    private static final int TOP_N = 10;
    private static final int DB_BATCH_SIZE = 10_000;

    private static final List<String> SHARD_KEYS = List.of(
            "popular:recruit:shard:0",
            "popular:recruit:shard:1",
            "popular:recruit:shard:2"
    );

    public void increaseRecruitScore(Long recruitId) {
        rankingService.increaseScore(recruitId);
    }

    public List<JobRecruitPopular> getPopularRecruits() {
        List<JobRecruitPopular> cached = popularCache.readCache(JobRecruitPopular.class);
        if (!cached.isEmpty()) return cached;

        String token = popularCache.tryLock();
        if (token != null) {
            try {
                refreshPopularRecruitsCache();
            } catch (
                    Exception e) {
                throw new RuntimeException(e);
            } finally {
                popularCache.unlock(token);
            }
            return popularCache.readCache(JobRecruitPopular.class);
        }
        return Collections.emptyList();
    }

    public void refreshPopularRecruitsCache() throws ExecutionException, InterruptedException {
        Set<String> topIdStrings = rankingService.getTopIds(TOP_N);
        if (topIdStrings == null || topIdStrings.isEmpty()) {
            popularCache.writeCache(Collections.emptyList());
            return;
        }

        List<Long> idList = topIdStrings.stream().map(Long::valueOf).toList();
        Map<Long, JobRecruitPopular> byId = jobRecruitPopularMapper.findPopularRecruits(idList)
                                                                   .stream()
                                                                   .collect(Collectors.toMap(JobRecruitPopular::getId, x -> x));

        List<JobRecruitPopular> ordered = topIdStrings.stream()
                                                      .map(Long::valueOf)
                                                      .map(byId::get)
                                                      .filter(Objects::nonNull)
                                                      .toList();

        popularCache.writeCache(ordered);
    }

    /**
     * Direct insert to database
     * */
    public void saveDailyHitsRecruits(LocalDate date) throws InterruptedException, ExecutionException {
        long totalStart = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(SHARD_KEYS.size());
        List<Future<?>> futures = new ArrayList<>();

        for (String shardKey : SHARD_KEYS) {
            long shardStart = System.currentTimeMillis();
            futures.add(executor.submit(() -> {
                rankingService.processZSetMembers(shardKey, batch -> {
                    List<JobRecruitDailyView> list = batch.stream()
                                                          .map(e -> new JobRecruitDailyView(
                                                                  Long.valueOf(e.id()),
                                                                  date,
                                                                  (long) e.score()
                                                          ))
                                                          .toList();
                    saveBatchToDB(list);
                });
            }));
            long shardEnd = System.currentTimeMillis();
            System.out.println("[Shard: " + shardKey + "] 처리 시간 = " + (shardEnd - shardStart) + " ms");
        }

        for (Future<?> f : futures) {
            f.get();
        }
        executor.shutdown();

        long totalEnd = System.currentTimeMillis();
        System.out.println("전체 처리 시간 = " + (totalEnd - totalStart) + "ms");
    }

    private void saveBatchToDB(List<JobRecruitDailyView> list) {
        int size = list.size();
        for (int i = 0; i < size; i += DB_BATCH_SIZE) {
            int end = Math.min(i + DB_BATCH_SIZE, size);
            List<JobRecruitDailyView> subList = list.subList(i, end);

            jobRecruitPopularMapper.updateDailyViews(subList);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Using kafka server
     * */
    public void produceDailyViews(LocalDate date) throws InterruptedException, ExecutionException {
        long totalStart = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(SHARD_KEYS.size());
        List<Future<?>> futures = new ArrayList<>();

        for (String shardKey : SHARD_KEYS) {
            long shardStart = System.currentTimeMillis();

            futures.add(executor.submit(() -> {
                rankingService.processZSetMembers(shardKey, batch -> {
                    List<JobRecruitDailyView> list = batch.stream()
                                                          .map(e -> new JobRecruitDailyView(
                                                                  Long.valueOf(e.id()),
                                                                  date,
                                                                  (long) e.score()
                                                          ))
                                                          .toList();

                    producer.sendDailyViews(list);
                });
            }));
            long shardEnd = System.currentTimeMillis();
            System.out.println("[Shard: " + shardKey + "] 처리 시간 = " + (shardEnd - shardStart) + " ms");
        }

        for (Future<?> f : futures) {
            f.get();
        }
        executor.shutdown();

        long totalEnd = System.currentTimeMillis();
        System.out.println("전체 처리 시간 = " + (totalEnd - totalStart) + "ms");
    }
}
