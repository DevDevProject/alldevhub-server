package com.example.kafka.service;

import com.example.kafka.repository.JobRecruitDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobRecruitDetailService {

    private final JobRecruitDetailRepository jobRecruitDetailRepository;


}
