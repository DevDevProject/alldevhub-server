package com.example.jobservice.controller;

import com.example.jobservice.dto.recruit.request.JobRecruitRequestDto;
import com.example.jobservice.dto.recruit.request.JobSearchCondition;
import com.example.jobservice.dto.recruit.response.JobRecruitListResponseDto;
import com.example.jobservice.dto.recruit.response.RecruitDetailResponseDto;
import com.example.jobservice.facade.RecruitFacadeService;
import com.example.jobservice.mapper.recruit.dto.JobRecruitPopular;
import com.example.jobservice.service.JobRecruitService;
import com.example.jobservice.service.redis.PopularService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/recruit")
@RequiredArgsConstructor
public class JobRecruitController {

    private final JobRecruitService jobRecruitService;
    private final RecruitFacadeService recruitFacadeService;
    private final PopularService popularService;

    @PostMapping(value = "/save/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> insertJobRecruitImage(@RequestParam("data") String request,
                                                   @RequestParam("image") MultipartFile image) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        JobRecruitRequestDto dto = objectMapper.readValue(request, JobRecruitRequestDto.class);

        jobRecruitService.saveOne(dto, image);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/save")
    public ResponseEntity<?> insertJobRecruits(@RequestBody JobRecruitRequestDto[] request) {
        jobRecruitService.save(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody JobSearchCondition condition, Pageable pageable, @RequestParam(defaultValue = "created_at") String sort) {
        JobRecruitListResponseDto response = jobRecruitService.search(condition, pageable, sort);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/url")
    public ResponseEntity<?> getRecruitsUrls() {
        List<String> response = jobRecruitService.getAllUrls();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{recruitId}")
    public ResponseEntity<?> getRecruitDetail(@PathVariable Long recruitId) {
        RecruitDetailResponseDto response = recruitFacadeService.getRecruitDetails(recruitId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{companyName}/recruits")
    public ResponseEntity<?> getRecruitsByCompany(@PathVariable String companyName, Pageable pageable) {
        JobRecruitListResponseDto response = jobRecruitService.getRecruitsByCompany(companyName, pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/popular")
    public ResponseEntity<?> getPopularRecruits() {
        List<JobRecruitPopular> response = popularService.getPopularRecruits();

        return ResponseEntity.ok(response);
    }
}
