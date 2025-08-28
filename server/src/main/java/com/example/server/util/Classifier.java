package com.example.server.util;

import com.example.server.mapper.CategoryMapper;
import com.example.server.mapper.CategoryStackMapper;
import com.example.server.mapper.StackMapper;
import com.example.server.vo.Category;
import com.example.server.vo.Stack;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Classifier {

    private final StackMapper stackMapper;
    private final CategoryMapper categoryMapper;
    private final CategoryStackMapper categoryStackMapper;

    private List<Stack> stacks;

    @PostConstruct
    public void loadStacks() {
        stacks = stackMapper.findAll();
    }

    public ClassificationResult classify(String text) {
        List<Stack> matchedStacks = stacks.stream()
                .filter(stack -> {
                    String keyword = stack.getName();
                    Pattern pattern = Pattern.compile("\\b" + Pattern.quote(keyword) + "\\b", Pattern.CASE_INSENSITIVE);
                    return pattern.matcher(text).find();
                })
                .toList();

        Set<Long> categoryIds = matchedStacks.stream()
                .flatMap(stack -> categoryStackMapper.findCategoriesByStackId(stack.getId()).stream())
                .map(Category::getId)
                .collect(Collectors.toSet());

        List<String> matchedCategories = categoryIds.stream()
                .map(categoryMapper::findById)
                .map(Category::getName)
                .distinct()
                .toList();

        return new ClassificationResult(matchedStacks, matchedCategories);
    }

    public List<Stack> classifyStack(String text) {
        return stacks.stream()
                .filter(stack -> {
                    String keyword = stack.getName();
                    Pattern pattern = Pattern.compile("\\b" + Pattern.quote(keyword) + "\\b", Pattern.CASE_INSENSITIVE);
                    return pattern.matcher(text).find();
                })
                .toList();
    }

    public static class ClassificationResult {
        private List<Stack> stacks;
        private List<String> categories;

        public ClassificationResult(List<Stack> stacks, List<String> categories) {
            this.stacks = stacks;
            this.categories = categories;
        }

        public List<Stack> getStacks() {
            return stacks;
        }

        public List<String> getCategories() {
            return categories;
        }
    }
}
