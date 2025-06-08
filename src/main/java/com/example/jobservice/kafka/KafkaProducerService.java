package com.example.jobservice.kafka;

import com.example.jobservice.kafka.dto.RecruitCountMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, RecruitCountMessage> kafkaTemplate;
    private static final String TOPIC = "company.recruit_count.increase";

    public void sendRecruitCount(RecruitCountMessage message) {
        kafkaTemplate.send(TOPIC, message);
    }
}
