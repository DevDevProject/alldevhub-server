package com.example.common.dto.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class RecruitCreatedMessage {

    @JsonProperty("job_id")
    private Long jobId;

    @JsonProperty("company_id")
    private Long companyId;

    @JsonProperty("company_name")
    private String companyName;

    private String title;
    private String type;
    private String workExperience;
    private String url;
    private String postingType;
    private String deadline;

    private String department;

    private String categories;

    private DetailInfo detail;

    private LocalDateTime createdAt;
    private String action;

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
