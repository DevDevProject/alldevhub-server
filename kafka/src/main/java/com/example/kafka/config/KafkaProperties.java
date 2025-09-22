package com.example.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaProperties {
    private Producer producer = new Producer();

    @Data
    public static class Producer {
        private String bootstrapServers;
        private int retries;
        private boolean enableIdempotence;
    }
}
