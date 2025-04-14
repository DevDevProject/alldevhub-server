package com.example.jobservice.dto.recruit.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class JobRecruitRequestDto {

    private BasicInfo basic;
    private DetailInfo detail;

    public JobRecruitRequestDto() {

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BasicInfo {
        private String title;
        private String workExperience;
        private String url;
        private String department;
        private String company;
        private String category;
        private String type;
        private String field;
        private String location;
        private String period;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetailInfo {
        private String responsibility;
        private String requirement;
        private String preference;
        private String benefit;
        private String process;
    }
}
