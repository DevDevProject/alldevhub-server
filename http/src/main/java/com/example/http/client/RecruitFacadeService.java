package com.example.http.client;

import java.util.List;

import com.example.common.dto.company.CompanyNameLogoResponse;
import com.example.http.company.CompanyServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
public class RecruitFacadeService {

    private final CompanyServiceClient companyServiceClient;

    public CompanyNameLogoResponse getCompanyNameLogo(String companyName, List<String> fields) {
        String joinedFields = String.join(",", fields);

        return companyServiceClient.getCompanyNameLogo(companyName, joinedFields);
    }
}
