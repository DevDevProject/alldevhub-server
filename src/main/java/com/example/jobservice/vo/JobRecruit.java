package com.example.jobservice.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class JobRecruit {

    private Long id;
    private String title;
    private String workExperience;
    private String url;

    private Long departmentId;
    private Long companyId;
    private Long typeId;
    private String deadline;
    private String postingType;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public JobRecruit(String title, String workExperience, String url, Long departmentId, Long companyId, Long typeId, String deadline, String postingType) {
        this.title = title;
        this.workExperience = workExperience;
        this.url = url;
        this.departmentId = departmentId;
        this.companyId = companyId;
        this.typeId = typeId;
        this.deadline = deadline;
        this.postingType = postingType;
    }
}
