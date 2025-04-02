package com.example.jobservice.service;

import com.example.jobservice.mapper.CategoryMapper;
import com.example.jobservice.vo.Category;
import com.example.jobservice.vo.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;

    public Long getCategoryId(String name) {
        Category category = categoryMapper.findByName(name);

        if(category != null)
            return category.getId();

        Category newCategory = Category.builder().name(name).build();

        categoryMapper.insert(newCategory);

        return categoryMapper.findByName(name).getId();
    }
}
