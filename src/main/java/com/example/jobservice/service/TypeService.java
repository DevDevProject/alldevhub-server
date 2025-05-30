package com.example.jobservice.service;

import com.example.jobservice.mapper.TypeMapper;
import com.example.jobservice.vo.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TypeService {

    private final TypeMapper typeMapper;

    public Long getTypeId(String name, String title) {
        Map<String, String> keywordToTypeName = Map.of(
                "인턴", "인턴",
                "계약", "계약직",
                "정규직", "정규직"
        );

        if(name == null) {
            for (Map.Entry<String, String> entry : keywordToTypeName.entrySet()) {
                if (title.contains(entry.getKey())) {
                    Type type = typeMapper.findByName(entry.getValue());
                    if (type != null) {
                        return type.getId();
                    }
                }
            }
        }


        Type type = typeMapper.findByName("정규직");

        return type.getId();
    }
}
