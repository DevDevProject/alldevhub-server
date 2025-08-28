package com.example.server.mapper;

import com.example.server.vo.Category;

import java.util.Arrays;
import java.util.List;

public interface CategoryStackMapper {
    List<Category> findCategoriesByStackId(Long id);
}
