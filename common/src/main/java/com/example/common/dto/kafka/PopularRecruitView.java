package com.example.common.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PopularRecruitView {
    private Long id;
    private double score;
}
