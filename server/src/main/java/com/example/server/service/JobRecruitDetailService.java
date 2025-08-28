package com.example.server.service;

import java.util.ArrayList;
import java.util.List;

import com.example.common.dto.company.CompanyNameLogoResponse;
import com.example.http.client.RecruitFacadeService;
import com.example.http.company.CompanyServiceClient;
import com.example.server.dto.recruit.request.JobRecruitRequestDto;
import com.example.server.dto.recruit.response.RecruitDetailResponseDto;
import com.example.server.dto.recruit.response.data.RecruitDetailDataDto;
import com.example.server.dto.recruit.response.data.RecruitOptionsDataDto;
import com.example.server.mapper.JobRecruitDetailMapper;
import com.example.server.vo.JobRecruitDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobRecruitDetailService {

    private final PopularService popularService;
    private final CompanyService companyService;
    private final RecruitFacadeService recruitFacadeService;
    private final JobRecruitDetailMapper jobRecruitDetailMapper;

    public void save(String bodyImageUrl, Long jobRecruitId) {
        JobRecruitDetail jobRecruitDetail = new JobRecruitDetail(bodyImageUrl, jobRecruitId);

        jobRecruitDetailMapper.insertByImage(jobRecruitDetail);
    }

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

    public RecruitDetailResponseDto getRecruitDetails(Long recruitId) {
        popularService.increaseRecruitScore(recruitId);

        String companyName = companyService.getCompanyName(recruitId);

        List<String> options = new ArrayList<>();
        options.add("name");
        options.add("logo_url");
        options.add("region");

        CompanyNameLogoResponse companyNameLogo = recruitFacadeService.getCompanyNameLogo(companyName, options);

        RecruitDetailDataDto details = getDetails(recruitId);

        RecruitOptionsDataDto option = getRecruitDetailOptions(recruitId);

        return RecruitDetailResponseDto.of(companyNameLogo, details, option);
    }
}
