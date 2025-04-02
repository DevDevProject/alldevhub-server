package com.example.jobservice.mapper;

import com.example.jobservice.vo.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

public interface CategoryMapper {

    Category findByName(@Param("name") String name);
    void insert(Category name);
}
