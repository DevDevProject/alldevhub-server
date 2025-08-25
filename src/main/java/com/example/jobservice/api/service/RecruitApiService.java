package com.example.jobservice.api.service;

import com.example.jobservice.api.mapper.RecruitApiMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitApiService {

    private final RecruitApiMapper recruitApiMapper;

    public List<Long> getAllIds() {
        return recruitApiMapper.findAllIds();
    }
}
