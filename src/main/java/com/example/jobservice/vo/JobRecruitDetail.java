package com.example.jobservice.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JobRecruitDetail {

    private Long id;
    private Long jobRecruitId;

    private String responsibility;
    private String requirement;
    private String preference;
    private String benefit;
    private String process;

    public JobRecruitDetail(String responsibility, String requirement, String preference, String benefit, String process, Long jobRecruitId) {
        this.jobRecruitId = jobRecruitId;
        this.responsibility = responsibility;
        this.requirement = requirement;
        this.preference = preference;
        this.benefit = benefit;
        this.process = process;
    }
}
