package com.example.kafka.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.kafka.dto.RecruitCreateDto;
import com.example.kafka.entity.JobRecruit;
import com.example.kafka.factory.EntityFactory;
import com.example.kafka.repository.JobRecruitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobRecruitService {

    private final EntityFactory entityFactory;
    private final JobRecruitRepository jobRecruitRepository;
    private final Logger logger = LoggerFactory.getLogger(JobRecruitService.class);

    @Transactional
    public void save(RecruitCreateDto dto) throws Exception {
        try {
            JobRecruit jobRecruit = entityFactory.makeRecruit(dto);

            jobRecruitRepository.save(jobRecruit);
        } catch (Exception e) {
            logger.error("DB insert failed . log : {}", dto.getBasic().getTitle(), e);
            throw e;
        }
    }

    @Transactional
    public void saveAll(List<RecruitCreateDto> dtos) throws Exception {
        try {
            List<JobRecruit> jobRecruits = dtos.stream()
                                               .map(entityFactory::makeRecruit)
                                               .collect(Collectors.toList());

            jobRecruitRepository.saveAll(jobRecruits);

        } catch (Exception e) {
            logger.error("DB batch insert failed for {} records.", dtos.size(), e);
            throw e;
        }
    }
}
