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

    public JobRecruit(String title, String workExperience, String url, Long departmentId, Long companyId, Long categoryId, Long typeId) {
        this.title = title;
        this.workExperience = workExperience;
        this.url = url;
        this.departmentId = departmentId;
        this.companyId = companyId;
        this.categoryId = categoryId;
        this.typeId = typeId;
    }
}
