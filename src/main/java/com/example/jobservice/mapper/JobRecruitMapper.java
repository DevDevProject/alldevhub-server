package com.example.jobservice.mapper;
import com.example.jobservice.vo.JobRecruit;
import com.example.jobservice.vo.jobrecruit.JobRecruitPaging;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobRecruitMapper {

    void insert(JobRecruit jobRecruit);

    List<JobRecruitPaging> findAll(@Param("pageable") Pageable pageable);
}
