package com.example.jobservice.dto.recruit.response;

import com.example.jobservice.dto.company.response.CompanyNameLogoResponse;
import com.example.jobservice.dto.recruit.response.data.RecruitDetailDataDto;
import com.example.jobservice.dto.recruit.response.data.RecruitOptionsDataDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitDetailResponseDto {

    private Company company;
    private Detail detail;
    private Options options;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Company {
        private String name;
        private String logo;
        private String location;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detail {
        private String responsibility;
        private String requirement;
        private String preference;
        private String benefit;
        private String process;
        private String bodyUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Options {
        private String title;
        private String url;
        private String deadline;
        private String experience;
        private String type;
        private String department;
        private String category;
    }

    public static RecruitDetailResponseDto of(
            CompanyNameLogoResponse company,
            RecruitDetailDataDto detail,
            RecruitOptionsDataDto options
    ) {
        return RecruitDetailResponseDto.builder()
                .company(
                        Company.builder()
                                .name(company.getName())
                                .logo(company.getLogoUrl())
                                .location(company.getLocation())
                                .build()
                )
                .detail(
                        Detail.builder()
                                .responsibility(detail.getResponsibility())
                                .requirement(detail.getRequirement())
                                .preference(detail.getPreference())
                                .benefit(detail.getBenefit())
                                .process(detail.getProcess())
                                .bodyUrl(detail.getBodyUrl())
                                .build()
                )
                .options(
                        Options.builder()
                                .title(options.getTitle())
                                .url(options.getUrl())
                                .deadline(options.getDeadline())
                                .experience(options.getExperience())
                                .type(options.getType())
                                .department(options.getDepartment())
                                .category(options.getCategory())
                                .build()
                )
                .build();
    }
}
