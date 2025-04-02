package com.example.jobservice.controller;

import com.example.jobservice.dto.recruit.request.JobRecruitRequestDto;
import com.example.jobservice.service.JobRecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JobRecruitController {

    private final JobRecruitService jobRecruitService;

    @PostMapping("/recruit/save")
    public ResponseEntity<?> insertJobRecruits(@RequestBody JobRecruitRequestDto[] request) {
        jobRecruitService.save(request);

        return ResponseEntity.ok().build();
    }
}
