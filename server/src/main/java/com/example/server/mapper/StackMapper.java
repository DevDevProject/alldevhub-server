package com.example.server.mapper;

import com.example.server.dto.stack.response.StackCountDto;
import com.example.server.vo.Stack;

import java.util.List;

public interface StackMapper {
    List<Stack> findAll();

    List<StackCountDto> findPopularStacks();

    int findTotalCount();
}
