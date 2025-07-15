package com.example.jobservice.service;

import com.example.jobservice.mapper.JobRecruitMapper;
import com.example.jobservice.mapper.recruit.JobRecruitPopularMapper;
import com.example.jobservice.mapper.recruit.dto.JobRecruitPopular;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PopularService {

    private final StringRedisTemplate redis;
    private final JobRecruitPopularMapper jobRecruitPopularMapper;

    public void increaseRecruitScore(Long recruitId) {
        redis.opsForZSet().incrementScore("popular:recruit", recruitId.toString(), 1);
    }

    public List<JobRecruitPopular> getPopularRecruits() {
        List<Long> ids = getPopularRecruitIds();

        return jobRecruitPopularMapper.findPopularRecruits(ids);
    }

    private List<Long> getPopularRecruitIds() {
        Set<String> ids = redis.opsForZSet().reverseRange("popular:recruit", 0, 9);
        return ids.stream().map(Long::valueOf).toList();
    }
}
