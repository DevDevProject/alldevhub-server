package com.example.server.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class JobRecruitStack {

    private Long JobRecruitId;
    private Long StackId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
