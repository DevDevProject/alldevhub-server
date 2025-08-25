package com.example.jobservice.scheduler;

import com.example.jobservice.service.redis.PopularService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisCacheScheduler {

    private final PopularService popularService;

    /**
     * 1 시간 주기로 인기글 갱신
     * */
    @Scheduled(fixedRate = 1_000 * 60 * 60)
    public void refresh() {
        popularService.refreshPopularRecruitsCache();
    }
}
