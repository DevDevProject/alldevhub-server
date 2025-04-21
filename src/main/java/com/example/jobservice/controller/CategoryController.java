package com.example.jobservice.controller;

import com.example.jobservice.dto.category.response.CategoryCountDto;
import com.example.jobservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/api/category/rank")
    public ResponseEntity<?> getPopularCategories() {
        List<CategoryCountDto> response = categoryService.getPopularCategories();

        return ResponseEntity.ok().body(response);
    }
}
