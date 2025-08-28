package com.example.server.mapper;

import com.example.server.vo.Company;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

public interface CompanyMapper {

    Company findByName(@Param("name") String name);
    void insert(Company name);

    String findByRecruitId(@Param("recruitId") Long recruitId);

}
