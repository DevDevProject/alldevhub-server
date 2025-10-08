package com.example.kafka.service;

import com.example.kafka.entity.Company;
import com.example.kafka.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public Long getCompanyId(String name) {
        Company company = companyRepository.findByName(name);

        if(company != null)
            return company.getId();

        Company newCompany = Company.builder().name(name).build();

        companyRepository.save(newCompany);

        return newCompany.getId();
    }
}
