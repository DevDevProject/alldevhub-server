package com.example.server.service;

import com.example.redis.popular.PopularCacheService;
import com.example.server.mapper.recruit.JobRecruitPopularMapper;
import com.example.server.mapper.recruit.dto.JobRecruitPopular;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PopularService {

    private final PopularCacheService popularCache;
    private final JobRecruitPopularMapper jobRecruitPopularMapper;

    private static final int TOP_N = 10;

    /** 점수 증가 */
    public void increaseRecruitScore(Long recruitId) {
        popularCache.increaseScore(recruitId);
    }

    /** 인기 공고 가져오기 */
    public List<JobRecruitPopular> getPopularRecruits() {
        List<JobRecruitPopular> cached = popularCache.readCache(JobRecruitPopular.class);
        if (!cached.isEmpty()) return cached;

        String token = popularCache.tryLock();
        if (token != null) {
            try {
                refreshPopularRecruitsCache();
            } finally {
                popularCache.unlock(token);
            }
            return popularCache.readCache(JobRecruitPopular.class);
        }
        return Collections.emptyList();
    }

    /** 캐시 갱신 */
    public void refreshPopularRecruitsCache() {
        Set<String> topIdStrings = popularCache.getTopIds(TOP_N);
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
}
