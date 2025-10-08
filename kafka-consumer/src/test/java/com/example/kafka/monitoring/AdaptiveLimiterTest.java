package com.example.kafka.monitoring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdaptiveLimiterTest {

    @Autowired
    private AdaptiveLimiter limiter;

//    @Test
//    void testPrometheusAdjustment() {
//        limiter.adjustRateWithPrometheus();
//        limiter.acquire();
//    }
}