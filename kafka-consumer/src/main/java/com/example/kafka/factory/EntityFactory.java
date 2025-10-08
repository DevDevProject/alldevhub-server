package com.example.kafka.factory;

import com.example.kafka.dto.RecruitCreateDto;
import com.example.kafka.entity.JobRecruit;
import com.example.kafka.service.CompanyService;
import com.example.kafka.service.DepartmentService;
import com.example.kafka.service.JobRecruitDetailService;
import com.example.kafka.service.JobRecruitService;
import com.example.kafka.service.TypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityFactory {

    private final TypeService typeService;
    private final CompanyService companyService;
    private final DepartmentService departmentService;

    public JobRecruit makeRecruit(RecruitCreateDto dto) {
        Long departmentId = departmentService.getDepartmentId(dto.getBasic().getDepartment());
        Long companyId = companyService.getCompanyId(dto.getBasic().getCompany());
        Long typeId = typeService.getTypeId(dto.getBasic().getType(), dto.getBasic().getTitle());

        return JobRecruit.builder()
                         .title(dto.getBasic().getTitle())
                         .workExperience(dto.getBasic().getWorkExperience())
                         .url(dto.getBasic().getUrl())
                         .departmentId(departmentId)
                         .companyId(companyId)
                         .typeId(typeId)
                         .deadline(dto.getBasic().getDeadline())
                         .postingType(dto.getBasic().getPostingType())
                         .build();
    }
}
