package com.example.kafka.service;

import java.time.LocalDate;
import java.util.Map;

import com.example.kafka.entity.JobRecruitDailyViews;
import com.example.kafka.repository.JobRecruitDailyViewsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DailyViewsService {

    private final JobRecruitDailyViewsRepository jobRecruitDailyViewsRepository;

    @Transactional
    public void batchUpsert(Map<String, Long> aggregate) {
        LocalDate viewDate = LocalDate.now().minusDays(1);

        for (Map.Entry<String, Long> entry : aggregate.entrySet()) {
            Long recruitId = Long.valueOf(entry.getKey());
            Long views = entry.getValue();

            JobRecruitDailyViews entity = jobRecruitDailyViewsRepository.findByRecruitIdAndViewDate(recruitId, viewDate)
                                                                        .map(existing -> {
                                             existing.setViews(views);
                                             return existing;
                                         })
                                                                        .orElseGet(() -> new JobRecruitDailyViews(recruitId, viewDate, views));

            jobRecruitDailyViewsRepository.save(entity);
        }
    }

    @Transactional
    public void batchInsert(Map<String, Long> aggregate) {
        LocalDate viewDate = LocalDate.now().minusDays(1);
        aggregate.forEach((key, value) -> {
            jobRecruitDailyViewsRepository.batchInsert(Long.valueOf(key), viewDate, value);
        });
    }
}
