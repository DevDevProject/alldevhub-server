package com.example.kafka.config;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

@EnableKafka
@Configuration
public class KafkaConfig {

    private final Logger log = LoggerFactory.getLogger(KafkaConfig.class);

    // DTO 타입 대신 Object 타입을 사용하도록 수정합니다.
    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String,Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:10000,localhost:10001,localhost:10002");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "recruit-service-new");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1000);

        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 60000);
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 60000);

        JsonDeserializer<Object> delegateJsonDeserializer = new JsonDeserializer<>(Object.class);
        delegateJsonDeserializer.setUseTypeHeaders(true);
        delegateJsonDeserializer.addTrustedPackages("*");

        ErrorHandlingDeserializer<Object> errorHandlingDeserializer =
                new ErrorHandlingDeserializer<>(delegateJsonDeserializer);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), errorHandlingDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> f = new ConcurrentKafkaListenerContainerFactory<>();

        f.setConsumerFactory(consumerFactory());
        f.setBatchListener(true);
        f.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        f.setConcurrency(60);
        f.getContainerProperties().setMissingTopicsFatal(false);

        return f;
    }

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {

        ConsumerRecordRecoverer recoverer = (record, ex) -> {
            try {
                // record.value()가 Object 타입이 되었습니다.
                String jsonMessage = objectMapper.writeValueAsString(record.value());
                kafkaTemplate.send("recruit.create.dlq", jsonMessage);
                log.warn("DLQ send success. partition: {}, offset: {}", record.partition(), record.offset());
            } catch (Exception e) {
                log.error("Important!! DLQ send failed", e);
            }
        };

        return new DefaultErrorHandler(recoverer, new FixedBackOff(500L, 3));
    }
}
