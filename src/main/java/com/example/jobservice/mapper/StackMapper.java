package com.example.jobservice.mapper;

import com.example.jobservice.dto.stack.response.StackCountDto;
import com.example.jobservice.vo.Stack;

import java.util.List;

public interface StackMapper {
    List<Stack> findAll();

    List<StackCountDto> findPopularStacks();

    int findTotalCount();
}
