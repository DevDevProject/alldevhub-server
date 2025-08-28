package com.example.server.mapper.recruit;

import com.example.server.mapper.recruit.dto.JobRecruitPopular;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JobRecruitPopularMapper {

    List<JobRecruitPopular> findPopularRecruits(@Param("ids") List<Long> ids);
}
