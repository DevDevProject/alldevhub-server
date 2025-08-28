package com.example.server.service;

import com.example.server.dto.stack.response.PopularStackResponseDto;
import com.example.server.dto.stack.response.StackCountDto;
import com.example.server.mapper.StackMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StackService {

    private final StackMapper stackMapper;

    public PopularStackResponseDto findPopular() {
        return new PopularStackResponseDto(stackMapper.findPopularStacks(), stackMapper.findTotalCount());
    }
}
