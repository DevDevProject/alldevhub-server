package com.example.jobservice.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Stack {

    private Long id;
    private String name;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
