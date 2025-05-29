package com.example.jobservice.service;

import com.example.jobservice.mapper.CompanyMapper;
import com.example.jobservice.vo.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyMapper companyMapper;

    public Long getCompanyId(String name) {
        Company company = companyMapper.findByName(name);

        if(company != null)
            return company.getId();

        Company newCompany = Company.builder().name(name).build();

        companyMapper.insert(newCompany);

        return companyMapper.findByName(name).getId();
    }

    public String getCompanyName(Long recruitId) {
        return companyMapper.findByRecruitId(recruitId);
    }
}
