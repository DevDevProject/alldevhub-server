package com.example.jobservice.service;

import com.example.jobservice.dto.recruit.request.JobRecruitRequestDto;
import com.example.jobservice.mapper.*;
import com.example.jobservice.vo.JobRecruit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    private final JobRecruitDetailService jobRecruitDetailService;

    @Transactional
    public void save(JobRecruitRequestDto[] requests) {
        for(JobRecruitRequestDto request : requests) {
            Long departmentId = departmentService.getDepartmentId(request.getBasic().getDepartment());
            Long companyId = companyService.getCompanyId(request.getBasic().getCompany());
            Long typeId = typeService.getTypeId(request.getBasic().getType());

            JobRecruit jobRecruit = new JobRecruit(request.getBasic().getTitle(), request.getBasic().getWorkExperience(), request.getBasic().getUrl(),
                    departmentId, companyId, typeId);

            jobRecruitMapper.insert(jobRecruit);

            jobRecruitDetailService.save(request, jobRecruit.getId());
            categoryService.save(request.getBasic().getCategory(), request.getDetail(), jobRecruit.getId());
        }
    }

    public void getJobRecruits(Pageable pageable) {
        List<JobRecruit> recruits = jobRecruitMapper.findAll(pageable);
    }
}
