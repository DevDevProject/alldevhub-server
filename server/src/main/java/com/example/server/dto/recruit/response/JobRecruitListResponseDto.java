package com.example.server.dto.recruit.response;

import com.example.server.vo.jobrecruit.JobRecruitPaging;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JobRecruitListResponseDto {

    private List<JobRecruitPaging> recruits;
    private int totalCount;
}
