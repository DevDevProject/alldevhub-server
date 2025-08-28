package com.example.server.service;

import com.example.server.dto.category.response.CategoryCountDto;
import com.example.server.dto.recruit.request.JobRecruitRequestDto;
import com.example.server.mapper.CategoryMapper;
import com.example.server.mapper.JobRecruitCategoryMapper;
import com.example.server.mapper.JobRecruitStackMapper;
import com.example.server.util.Classifier;
import com.example.server.vo.Category;
import com.example.server.vo.Stack;
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

    public void save(String categoryName, String body, Long jobRecruitId) {
        if(categoryName == null) {
            Classifier.ClassificationResult result = classifier.classify(body);

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

        List<Stack> stacks = classifier.classifyStack(body);
        stacks.stream()
                .distinct()
                .forEach(stack -> {
                    jobRecruitStackMapper.insert(stack, jobRecruitId);
                });
    }

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
