package com.example.jobservice.mapper;

import com.example.jobservice.vo.Type;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

public interface TypeMapper {

    Type findByName(@Param("name") String name);
    void insert(Type name);
}
