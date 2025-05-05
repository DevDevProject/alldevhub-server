package com.example.jobservice.dto.recruit.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class JobSearchCondition {
    private List<String> stacks = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private List<String> companyTypes = new ArrayList<>();
    private List<String> regions = new ArrayList<>();
    private List<String> workExperiences = new ArrayList<>();

    public void setStacks(List<String> stacks) {
        this.stacks = new ArrayList<>(stacks);
    }

    public void setCategories(List<String> categories) {
        this.categories = new ArrayList<>(categories); // 복사해서 가변 리스트로
    }

    public void setCompanyTypes(List<String> companyTypes) {
        this.companyTypes = new ArrayList<>(companyTypes); // 복사해서 가변 리스트로
    }

    public void setRegions(List<String> regions) {
        this.regions = new ArrayList<>(regions); // 복사해서 가변 리스트로
    }

    public void workExperiences(List<String> workExperiences) {
        this.workExperiences = new ArrayList<>(workExperiences);
    }
}
