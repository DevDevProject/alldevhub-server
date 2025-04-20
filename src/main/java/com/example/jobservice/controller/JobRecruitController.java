package com.example.jobservice.controller;

import com.example.jobservice.dto.recruit.request.JobRecruitRequestDto;
import com.example.jobservice.service.JobRecruitService;
import com.example.jobservice.vo.JobRecruit;
import com.example.jobservice.vo.jobrecruit.JobRecruitPaging;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class JobRecruitController {

    private final JobRecruitService jobRecruitService;

    @PostMapping("/recruit/save")
    public ResponseEntity<?> insertJobRecruits(@RequestBody JobRecruitRequestDto[] request) {
        jobRecruitService.save(request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/recruit/list")
    public ResponseEntity<?> getJobRecruits(Pageable pageable) {
        List<JobRecruitPaging> jobRecruits = jobRecruitService.getJobRecruits(pageable);

        return ResponseEntity.ok().body(jobRecruits);
    }
}
