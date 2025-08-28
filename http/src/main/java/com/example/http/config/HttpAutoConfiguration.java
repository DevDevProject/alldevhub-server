package com.example.http.config;

import com.example.http.client.RecruitFacadeService;
import com.example.http.company.CompanyServiceClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.example.http.company")
public class HttpAutoConfiguration {

    @Bean
    public RecruitFacadeService recruitFacadeService(CompanyServiceClient companyServiceClient) {
        return new RecruitFacadeService(companyServiceClient);
    }
}