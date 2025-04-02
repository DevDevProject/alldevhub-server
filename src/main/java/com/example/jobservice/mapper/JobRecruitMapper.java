package com.example.jobservice.mapper;
import com.example.jobservice.vo.JobRecruit;
import org.apache.ibatis.annotations.Mapper;

public interface JobRecruitMapper {

    void insert(JobRecruit jobRecruit);
}
