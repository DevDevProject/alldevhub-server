package com.example.kafka.producer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.example.common.dto.common.JobRecruitDailyView;
import com.example.common.dto.kafka.PopularRecruitView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
public class PopularProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper mapper;
    private final Path dlqDir;

    private static final String TOPIC = "daily-views";

    /* TODO: Message will exchange to protobuf.
    *        Now using JSON
    * */
    public void sendDailyViews(List<JobRecruitDailyView> list) {
        list.forEach(v -> {
            try {
                String id = String.valueOf(v.getRecruitId());
                String value = mapper.writeValueAsString(Map.of(
                        "id", v.getRecruitId(),
                        "score", v.getViews(),
                        "date", v.getViewDate(),
                        "ts", Instant.now().toString()
                ));
                // key로 id를 설정해서 같은 id는 같은 파티션으로 가게 한다 — ordering/aggregation 보장
                kafkaTemplate.send(TOPIC, id, value)
                             .whenComplete((result, ex) -> {
                                 if (ex != null) {
                                     log.error("Kafka send failed for id={} : {}", id, ex.toString());
                                     writeToDlq(TOPIC, id, value, ex);
                                 }
                             });
            } catch (JsonProcessingException e) {
                log.error("JSON serialize error for id={}", String.valueOf(v.getRecruitId()), e);
            }
        });
    }

    private void writeToDlq(String topic, String key, String value, Throwable ex) {
        try {
            String fileName = Instant.now().toEpochMilli() + "-" + UUID.randomUUID() + ".json";
            Path p = dlqDir.resolve(fileName);
            String record = mapper.writeValueAsString(Map.of(
                    "topic", topic, "key", key, "value", value, "error", ex.toString(), "ts", Instant.now().toString()
            ));
            Files.writeString(p, record, StandardOpenOption.CREATE_NEW);
        } catch (IOException ignored) {}
    }
}
