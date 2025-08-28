package com.example.server.mapper;

import com.example.server.dto.recruit.response.RecruitDetailResponseDto;
import com.example.server.dto.recruit.response.data.RecruitDetailDataDto;
import com.example.server.dto.recruit.response.data.RecruitOptionsDataDto;
import com.example.server.vo.JobRecruitDetail;
import org.apache.ibatis.annotations.Param;

public interface JobRecruitDetailMapper {

    void insert(JobRecruitDetail jobRecruitDetail);

    RecruitDetailDataDto findRecruitDetail(@Param("recruitId") Long recruitId);

    RecruitOptionsDataDto findRecruitOptions(@Param("recruitId") Long recruitId);

    void insertByImage(JobRecruitDetail jobRecruitDetail);
}
