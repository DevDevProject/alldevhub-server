package com.example.jobservice.service;

import com.example.jobservice.dto.recruit.request.JobRecruitRequestDto;
import com.example.jobservice.mapper.*;
import com.example.jobservice.vo.JobRecruit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobRecruitService {

    private final JobRecruitMapper jobRecruitMapper;

    private final TypeService typeService;
    private final CompanyService companyService;
    private final CategoryService categoryService;
    private final DepartmentService departmentService;

    @Transactional
    public void save(JobRecruitRequestDto[] requests) {
        for(JobRecruitRequestDto request : requests) {
            Long departmentId = departmentService.getDepartmentId(request.getDepartment());
            Long companyId = companyService.getCompanyId(request.getCompany());
            Long categoryId = categoryService.getCategoryId(request.getCategory());
            Long typeId = typeService.getTypeId(request.getType());

            JobRecruit jobRecruit = new JobRecruit(request.getTitle(), request.getWorkExperience(), request.getUrl(),
                    departmentId, companyId, categoryId, typeId);

            jobRecruitMapper.insert(jobRecruit);
        }
    }
}
