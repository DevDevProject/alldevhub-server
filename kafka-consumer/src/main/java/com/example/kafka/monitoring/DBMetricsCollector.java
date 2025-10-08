package com.example.kafka.monitoring;

import java.util.concurrent.atomic.AtomicInteger;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DBMetricsCollector {

    private final HikariDataSource dataSource;
    private final AtomicInteger recentFailures = new AtomicInteger(0);

    public double getDbLoadScore() {
        int active = dataSource.getHikariPoolMXBean().getActiveConnections();
        int max = dataSource.getMaximumPoolSize();

        return Math.min(1.0, active / (double) max);
    }

    public void recordFailure() {
        recentFailures.incrementAndGet();
    }

    public double getFailureRate() {
        double rate = recentFailures.get() / 100.0;
        recentFailures.set(0);
        return rate;
    }
}
