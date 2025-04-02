package com.example.jobservice.service;

import com.example.jobservice.mapper.TypeMapper;
import com.example.jobservice.vo.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TypeService {

    private final TypeMapper typeMapper;

    public Long getTypeId(String name) {
        Type type = typeMapper.findByName(name);

        if(type != null)
            return type.getId();

        Type newType = Type.builder().name(name).build();

        typeMapper.insert(newType);

        return typeMapper.findByName(name).getId();
    }
}
