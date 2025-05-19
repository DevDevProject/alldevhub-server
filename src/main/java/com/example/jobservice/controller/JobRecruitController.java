package com.example.jobservice.controller;

import com.example.jobservice.dto.recruit.request.JobRecruitRequestDto;
import com.example.jobservice.dto.recruit.request.JobSearchCondition;
import com.example.jobservice.dto.recruit.response.JobRecruitListResponseDto;
import com.example.jobservice.service.JobRecruitService;
import com.example.jobservice.vo.jobrecruit.JobRecruitPaging;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class JobRecruitController {

    private final JobRecruitService jobRecruitService;

    @PostMapping("/api/recruit/save")
    public ResponseEntity<?> insertJobRecruits(@RequestBody JobRecruitRequestDto[] request) {
        jobRecruitService.save(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/recruit/search")
    public ResponseEntity<?> search(@RequestBody JobSearchCondition condition, Pageable pageable, @RequestParam(defaultValue = "created_at") String sort) {
        JobRecruitListResponseDto response = jobRecruitService.search(condition, pageable, sort);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/recruit/url")
    public ResponseEntity<?> getRecruitsUrls() {
        List<String> response = jobRecruitService.getAllUrls();

        return ResponseEntity.ok(response);
    }
}
