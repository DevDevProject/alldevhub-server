package com.example.server.mapper;

import com.example.server.dto.category.response.CategoryCountDto;
import com.example.server.vo.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryMapper {

    Category findByName(@Param("name") String name);
    void insert(Category name);

    Category findById(Long id);

    List<CategoryCountDto> findPopularCategories();
}
