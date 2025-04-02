package com.example.jobservice.vo;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    private Long id;
    private String name;
    private String location;
    private String industry;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
