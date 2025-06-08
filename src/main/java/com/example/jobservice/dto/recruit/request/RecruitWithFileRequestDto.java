package com.example.jobservice.dto.recruit.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecruitWithFileRequestDto {
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
    private String textBody;
}
