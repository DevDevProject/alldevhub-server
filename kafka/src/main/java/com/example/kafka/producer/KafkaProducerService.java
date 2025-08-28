package com.example.kafka.producer;

import com.example.common.dto.kafka.RecruitCountMessage;
import com.example.common.dto.kafka.RecruitCreatedMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

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
