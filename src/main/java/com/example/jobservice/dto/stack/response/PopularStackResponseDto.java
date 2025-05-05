package com.example.jobservice.dto.stack.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PopularStackResponseDto {

    private List<StackCountDto> stacks;
    private int count;
}
