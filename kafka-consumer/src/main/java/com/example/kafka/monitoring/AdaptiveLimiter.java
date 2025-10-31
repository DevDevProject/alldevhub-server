package com.example.kafka.monitoring;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

import com.google.common.util.concurrent.RateLimiter;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;

/**
 * Prometheus(mysqld_exporter, node_exporter) 방식 사용
 * */
@Component
@RequiredArgsConstructor
public class AdaptiveLimiter {

    private final AtomicInteger limit = new AtomicInteger(100);

    private final int MIN_LIMIT = 10;
    private final int MAX_LIMIT = 150;

    private long lastAdjustTime = System.currentTimeMillis();

    public void adjust(double dbLoadScore, double failureRate) {
        long now = System.currentTimeMillis();

        if (now - lastAdjustTime < 1000)
            return;

        lastAdjustTime = now;

        double loadFactor = 1.0 - dbLoadScore;
        double failureFactor = 1.0 - failureRate;
        double adjustFactor = (loadFactor + failureFactor) / 2.0;

        int newLimit = (int) (MIN_LIMIT + adjustFactor * (MAX_LIMIT - MIN_LIMIT));
        limit.set(newLimit);
    }

    public int getLimit() {
        return limit.get();
    }
}
