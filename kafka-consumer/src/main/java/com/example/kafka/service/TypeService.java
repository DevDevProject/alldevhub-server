package com.example.kafka.service;

import java.util.Map;

import com.example.kafka.entity.Type;
import com.example.kafka.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TypeService {

    private final TypeRepository typeRepository;

    public Long getTypeId(String name, String title) {
        Map<String, String> keywordToTypeName = Map.of(
                "인턴", "인턴",
                "계약", "계약직",
                "정규직", "정규직"
        );

        if(name == null) {
            for (Map.Entry<String, String> entry : keywordToTypeName.entrySet()) {
                if (title.contains(entry.getKey())) {
                    Type type = typeRepository.findByName(entry.getValue());
                    if (type != null) {
                        return type.getId();
                    }
                }
            }
        }


        Type type = typeRepository.findByName("정규직");

        return type.getId();
    }
}
