package com.example.jobservice.http;

import com.example.jobservice.dto.company.response.CompanyNameLogoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "company", url = "${company.service.url}")
public interface CompanyServiceClient {

    @GetMapping("/companies/single")
    CompanyNameLogoResponse getCompanyNameLogo(
            @RequestParam("name") String name,
            @RequestParam("fields") String fields);

}
