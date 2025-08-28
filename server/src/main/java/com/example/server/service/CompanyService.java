package com.example.server.service;

import com.example.server.mapper.CompanyMapper;
import com.example.server.vo.Company;
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
