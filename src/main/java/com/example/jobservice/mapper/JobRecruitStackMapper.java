package com.example.jobservice.mapper;

import com.example.jobservice.vo.Stack;
import org.apache.ibatis.annotations.Param;

public interface JobRecruitStackMapper {
    void insert(@Param("stack") Stack stack, @Param("id") Long id);
}
