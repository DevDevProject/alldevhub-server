package com.example.jobservice.service;

import com.example.jobservice.dto.recruit.request.JobRecruitRequestDto;
import com.example.jobservice.dto.recruit.response.RecruitDetailResponseDto;
import com.example.jobservice.dto.recruit.response.data.RecruitDetailDataDto;
import com.example.jobservice.dto.recruit.response.data.RecruitOptionsDataDto;
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

    public RecruitDetailDataDto getDetails(Long jobRecruitId) {
        return jobRecruitDetailMapper.findRecruitDetail(jobRecruitId);
    }

    public RecruitOptionsDataDto getRecruitDetailOptions(Long recruitId) {
        return jobRecruitDetailMapper.findRecruitOptions(recruitId);
    }
}
