package com.example.jobservice.mapper;

import com.example.jobservice.dto.recruit.response.RecruitDetailResponseDto;
import com.example.jobservice.dto.recruit.response.data.RecruitDetailDataDto;
import com.example.jobservice.dto.recruit.response.data.RecruitOptionsDataDto;
import com.example.jobservice.vo.JobRecruitDetail;
import org.apache.ibatis.annotations.Param;

public interface JobRecruitDetailMapper {

    void insert(JobRecruitDetail jobRecruitDetail);

    RecruitDetailDataDto findRecruitDetail(@Param("recruitId") Long recruitId);

    RecruitOptionsDataDto findRecruitOptions(@Param("recruitId") Long recruitId);
}
