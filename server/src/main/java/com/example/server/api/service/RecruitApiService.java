package com.example.server.api.service;

import com.example.server.api.mapper.RecruitApiMapper;
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
