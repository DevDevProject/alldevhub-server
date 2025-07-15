package com.example.jobservice.facade;

import com.example.jobservice.dto.company.response.CompanyNameLogoResponse;
import com.example.jobservice.dto.recruit.response.RecruitDetailResponseDto;
import com.example.jobservice.dto.recruit.response.data.RecruitDetailDataDto;
import com.example.jobservice.dto.recruit.response.data.RecruitOptionsDataDto;
import com.example.jobservice.http.CompanyServiceClient;
import com.example.jobservice.service.CompanyService;
import com.example.jobservice.service.JobRecruitDetailService;
import com.example.jobservice.service.JobRecruitService;
import com.example.jobservice.service.PopularService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruitFacadeService {

    private final JobRecruitService jobRecruitService;
    private final JobRecruitDetailService jobRecruitDetailService;
    private final CompanyService companyService;
    private final CompanyServiceClient companyServiceClient;
    private final PopularService popularService;

    public RecruitDetailResponseDto getRecruitDetails(Long recruitId) {
        popularService.increaseRecruitScore(recruitId);

        String companyName = companyService.getCompanyName(recruitId);

        CompanyNameLogoResponse company = companyServiceClient.getCompanyNameLogo(companyName, "name,logo_url,region");

        RecruitDetailDataDto detail = jobRecruitService.getRecruitDetail(recruitId);

        RecruitOptionsDataDto options = jobRecruitDetailService.getRecruitDetailOptions(recruitId);

        return RecruitDetailResponseDto.of(company, detail, options);
    }
}
