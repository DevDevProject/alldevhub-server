package com.example.server.api.controller;

import java.util.List;

import com.example.server.api.service.RecruitApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recruit/v1")
@RequiredArgsConstructor
public class RecruitApiController {

    private final RecruitApiService recruitApiService;

    @GetMapping("/ids")
    public ResponseEntity<?> getAllIds() {
        List<Long> response = recruitApiService.getAllIds();

        return ResponseEntity.ok(response);
    }
}
