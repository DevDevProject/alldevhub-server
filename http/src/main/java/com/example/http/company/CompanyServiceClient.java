package com.example.http.company;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "company", url = "${company.service.url}")
public interface CompanyServiceClient {

    @GetMapping("/companies/single")
    com.example.common.dto.company.CompanyNameLogoResponse getCompanyNameLogo(
            @RequestParam("name") String name,
            @RequestParam("fields") String fields);

}
