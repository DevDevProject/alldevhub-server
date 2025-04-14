package com.example.jobservice.service;

import com.example.jobservice.dto.recruit.request.JobRecruitRequestDto;
import com.example.jobservice.mapper.JobRecruitDetailMapper;
import com.example.jobservice.vo.JobRecruitDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobRecruitDetailService {

    private final JobRecruitDetailMapper jobRecruitDetailMapper;

    public void save(JobRecruitRequestDto request, Long jobRecruitId) {

        JobRecruitDetail jobRecruitDetail = new JobRecruitDetail(request.getDetail().getResponsibility(),
                request.getDetail().getRequirement(), request.getDetail().getPreference(),
                request.getDetail().getBenefit(), request.getDetail().getProcess(), jobRecruitId);

        jobRecruitDetailMapper.insert(jobRecruitDetail);
    }
}
