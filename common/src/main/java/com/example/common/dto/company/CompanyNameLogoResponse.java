package com.example.common.dto.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyNameLogoResponse {

    private Long id;
    private String name;
    private String logoUrl;

    @JsonProperty("region")
    private String location;
}
