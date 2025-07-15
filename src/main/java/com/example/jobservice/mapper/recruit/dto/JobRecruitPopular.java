package com.example.jobservice.mapper.recruit.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobRecruitPopular {

    private Long id;
    private String title;
    private String url;
    private String workExperience;
    private String deadline;
    private String companyName;
}
