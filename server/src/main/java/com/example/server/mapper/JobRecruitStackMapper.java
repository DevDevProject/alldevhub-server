package com.example.server.mapper;

import com.example.server.vo.Stack;
import org.apache.ibatis.annotations.Param;

public interface JobRecruitStackMapper {
    void insert(@Param("stack") Stack stack, @Param("id") Long id);
}
