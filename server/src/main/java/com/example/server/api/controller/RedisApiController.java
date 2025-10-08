package com.example.server.api.controller;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

import com.example.server.service.PopularService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/redis/v1")
@RequiredArgsConstructor
public class RedisApiController {

    private final PopularService popularService;

    @GetMapping("/save")
    public void redis() throws ExecutionException, InterruptedException {
        popularService.saveDailyHitsRecruits(LocalDate.now());
    }
}
