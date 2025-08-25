package com.example.jobservice.api.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface RecruitApiMapper {

    List<Long> findAllIds();
}
