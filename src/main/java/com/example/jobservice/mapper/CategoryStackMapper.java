package com.example.jobservice.mapper;

import com.example.jobservice.vo.Category;

import java.util.Arrays;
import java.util.List;

public interface CategoryStackMapper {
    List<Category> findCategoriesByStackId(Long id);
}
