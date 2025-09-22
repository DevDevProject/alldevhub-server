package com.example.infra.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThreadConfiguration {

    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;

    @Bean
    public ExecutorService dbWriteExecutor() {
        return Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }
}
