package com.example.server.mapper.recruit;

import com.example.common.dto.common.JobRecruitDailyView;
import com.example.server.mapper.recruit.dto.JobRecruitPopular;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface JobRecruitPopularMapper {

    List<JobRecruitPopular> findPopularRecruits(@Param("ids") List<Long> ids);

    void updateDailyViews(List<JobRecruitDailyView> list);

    void insertOrUpdate(JobRecruitDailyView view);
}
