package com.example.jobservice.dto.company.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyNameLogoResponse {

    private Long id;
    private String name;
    private String logoUrl;

    @JsonProperty("region")
    private String location;
}
