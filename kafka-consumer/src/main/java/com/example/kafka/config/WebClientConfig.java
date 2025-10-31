package com.example.kafka.config;

import java.util.HashMap;

import com.google.common.net.HttpHeaders;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.web.reactive.function.client.WebClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;

@Configuration
@SuppressWarnings("UnstableApiUsage")
public class WebClientConfig {

    @Value("${prometheus.server.url}")
    private String prometheusUrl;

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                .baseUrl(prometheusUrl)
                .build();
    }

    @Bean
    public RateLimiter rateLimiter() {
        return RateLimiter.create(100.0);
    }

    @Bean // CloudWatchClient 객체를 Spring 빈으로 등록합니다.
    public CloudWatchClient cloudWatchClient() {
        String awsRegion = "ap-northeast-2";
        return CloudWatchClient.builder()
                               .region(Region.of(awsRegion))
                               .build();
    }
}
