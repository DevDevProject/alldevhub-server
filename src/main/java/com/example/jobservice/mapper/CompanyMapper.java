package com.example.jobservice.mapper;

import com.example.jobservice.vo.Company;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

public interface CompanyMapper {

    Company findByName(@Param("name") String name);
    void insert(Company name);

    String findByRecruitId(@Param("recruitId") Long recruitId);

}
