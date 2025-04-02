package com.example.jobservice.vo;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Type {

    private Long id;
    private String name;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
