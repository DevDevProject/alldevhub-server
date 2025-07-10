package com.example.jobservice.kafka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

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

    private LocalDateTime createdAt;

    private String action;
}
