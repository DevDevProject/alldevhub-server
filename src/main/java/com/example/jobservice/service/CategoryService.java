package com.example.jobservice.service;

import com.example.jobservice.dto.category.response.CategoryCountDto;
import com.example.jobservice.dto.recruit.request.JobRecruitRequestDto;
import com.example.jobservice.mapper.CategoryMapper;
import com.example.jobservice.mapper.JobRecruitCategoryMapper;
import com.example.jobservice.mapper.JobRecruitStackMapper;
import com.example.jobservice.util.Classifier;
import com.example.jobservice.vo.Category;
import com.example.jobservice.vo.Stack;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;
    private final Classifier classifier;
    private final JobRecruitCategoryMapper jobRecruitCategoryMapper;
    private final JobRecruitStackMapper jobRecruitStackMapper;

    public void save(String categoryName, JobRecruitRequestDto.DetailInfo detail, Long jobRecruitId) {
        if (categoryName == null) {
            String merge = Stream.of(
                            detail.getRequirement(),
                            detail.getPreference(),
                            detail.getResponsibility()
                    ).filter(Objects::nonNull)
                    .collect(Collectors.joining(" "));

            Classifier.ClassificationResult result = classifier.classify(merge);

            result.getCategories().stream()
                    .distinct()
                    .forEach(name -> {
                        Category category = categoryMapper.findByName(name);
                        if (category != null)
                            jobRecruitCategoryMapper.insert(jobRecruitId, category.getId());
                    });

            result.getStacks().stream()
                    .distinct()
                    .forEach(stack -> {
                        jobRecruitStackMapper.insert(stack, jobRecruitId);
                    });

            return;
        }

        Category category = categoryMapper.findByName(categoryName);
        if (category != null)
            jobRecruitCategoryMapper.insert(jobRecruitId, category.getId());

        String merge = Stream.of(
                        detail.getRequirement(),
                        detail.getPreference(),
                        detail.getResponsibility()
                ).filter(Objects::nonNull)
                .collect(Collectors.joining(" "));

        List<Stack> stacks = classifier.classifyStack(merge);
        stacks.stream()
                .distinct()
                .forEach(stack -> {
                    jobRecruitStackMapper.insert(stack, jobRecruitId);
                });
    }

    public List<CategoryCountDto> getPopularCategories() {
        return categoryMapper.findPopularCategories();
    }
}
