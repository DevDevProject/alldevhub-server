package com.example.common.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class RecruitCreateDto {
    private BasicInfo basic;
    private DetailInfo detail;

    public RecruitCreateDto() {

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
        private String deadline;
        private String postingType;
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
        private String body;
    }
}
