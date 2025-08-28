package com.example.server.mapper.recruit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobRecruitPopular {

    private Long id;
    private String title;
    private String url;
    private String workExperience;
    private String deadline;
    private String companyName;
}
