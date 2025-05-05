package com.example.jobservice.controller;

import com.example.jobservice.dto.stack.response.PopularStackResponseDto;
import com.example.jobservice.dto.stack.response.StackCountDto;
import com.example.jobservice.service.StackService;
import com.example.jobservice.vo.jobrecruit.JobRecruitPaging;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StackController {

    private final StackService stackService;

    @GetMapping("/api/stack/rank")
    public ResponseEntity<?> getPopularStacks() {
        PopularStackResponseDto response = stackService.findPopular();

        return ResponseEntity.ok().body(response);
    }
}
