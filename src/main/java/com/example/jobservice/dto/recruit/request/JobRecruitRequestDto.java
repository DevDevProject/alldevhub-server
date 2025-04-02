package com.example.jobservice.dto.recruit.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobRecruitRequestDto {

    private String title;
    private String workExperience;
    private String url;

    private String department;
    private String company;
    private String category;
    private String type;
}
