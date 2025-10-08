package com.example.kafka.entity;

import java.time.LocalDateTime;

import com.example.kafka.dto.RecruitCreateDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "job_recruit")
public class JobRecruit extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Column(name = "work_experience")
    private String workExperience;
    private String url;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "type_id")
    private Long typeId;
    private String deadline;

    @Column(name = "posting_type")
    private String postingType;

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