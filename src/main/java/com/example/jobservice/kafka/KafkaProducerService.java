package com.example.jobservice.kafka;

import com.example.jobservice.kafka.dto.RecruitCountMessage;
import com.example.jobservice.kafka.dto.RecruitCreatedMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String RECRUIT_COUNT_TOPIC = "company.recruit_count.increase";
    private static final String RECRUIT_CREATED_TOPIC = "company.recruit.created";

    public void sendRecruitCountEvent(RecruitCountMessage message) {
        kafkaTemplate.send(RECRUIT_COUNT_TOPIC, message);
    }

    public void sendRecruitCreatedEvent(RecruitCreatedMessage message) {
        kafkaTemplate.send(RECRUIT_CREATED_TOPIC, message);
    }
}
