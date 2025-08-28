package com.example.common.dto.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecruitCountMessage {

    @JsonProperty("company_name")
    private String companyName;
    private String action;
}
