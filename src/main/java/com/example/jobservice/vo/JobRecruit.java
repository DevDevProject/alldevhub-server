package com.example.jobservice.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class JobRecruit {

    private Long id;
    private String title;
    private String workExperience;
    private String url;

    private Long departmentId;
    private Long companyId;
    private Long categoryId;
    private Long typeId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
