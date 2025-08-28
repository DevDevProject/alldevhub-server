package com.example.server.controller;

import com.example.server.dto.stack.response.PopularStackResponseDto;
import com.example.server.dto.stack.response.StackCountDto;
import com.example.server.service.StackService;
import com.example.server.vo.jobrecruit.JobRecruitPaging;
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

    @GetMapping("/api/recruit/stack/rank")
    public ResponseEntity<?> getPopularStacks() {
        PopularStackResponseDto response = stackService.findPopular();

        return ResponseEntity.ok().body(response);
    }
}
