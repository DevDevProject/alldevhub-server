package com.example.kafka.config;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.example.kafka.producer.KafkaProducerService;
import com.example.kafka.producer.PopularProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@RequiredArgsConstructor
//@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaAutoConfiguration {

//    private final KafkaProperties kafkaProperties;
//
//    @Bean
//    public ProducerFactory<String, String> stringProducerFactory() {
//        Map<String, Object> config = new HashMap<>();
//        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getProducer().getBootstrapServers());
//        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
////        config.put("spring.json.trusted.packages", "*");
//
//        config.put(ProducerConfig.ACKS_CONFIG, "all");
//        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
//        config.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
//        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
//        config.put(ProducerConfig.LINGER_MS_CONFIG, 100);
//        config.put(ProducerConfig.BATCH_SIZE_CONFIG, 393216);
//        config.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
//
//        return new DefaultKafkaProducerFactory<>(config);
//    }
//
//    @Bean
//    public ProducerFactory<String, Object> objectProducerFactory() {
//        Map<String, Object> config = new HashMap<>();
//        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getProducer().getBootstrapServers());
//        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
////        config.put("spring.json.trusted.packages", "*");
//
//        config.put(ProducerConfig.ACKS_CONFIG, "all");
//        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
//        config.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
//        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
//        config.put(ProducerConfig.LINGER_MS_CONFIG, 100);
//        config.put(ProducerConfig.BATCH_SIZE_CONFIG, 393216);
//        config.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
//
//        return new DefaultKafkaProducerFactory<>(config);
//    }
    private final org.springframework.boot.autoconfigure.kafka.KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<String, String> stringProducerFactory() {
        Map<String, Object> config = new HashMap<>();
        // spring boot auto-configured properties 사용
        config.putAll(kafkaProperties.buildProducerProperties());

        // 원하는 추가 설정만 추가
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public ProducerFactory<String, Object> objectProducerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.putAll(kafkaProperties.buildProducerProperties());

        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }


    @Bean
    public KafkaTemplate<String, String> stringKafkaTemplate(@Qualifier("stringProducerFactory") ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public KafkaTemplate<String, Object> objectKafkaTemplate(@Qualifier("objectProducerFactory") ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public KafkaProducerService kafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        return new KafkaProducerService(kafkaTemplate);
    }

    @Bean
    public PopularProducer popularProducer(
            @Qualifier("stringKafkaTemplate") KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper,
            @Value("${app.dlq.producer.path:/var/alldevhub/dlq/producer}") String dlqPath) {

        return new PopularProducer(kafkaTemplate, objectMapper, Paths.get(dlqPath));
    }
}
