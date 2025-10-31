package com.example.kafka.config;

import javax.sql.DataSource;

import java.util.Properties;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.kafka.repository",
        entityManagerFactoryRef = "recruitEntityManagerFactory",
        transactionManagerRef = "recruitTransactionManager"
)
public class RecruitDBConfig {

    @Bean
    @ConfigurationProperties(prefix = "recruit.datasource")
    public DataSourceProperties recruitDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean
    public DataSource recruitDataSource(
            @Qualifier("recruitDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Primary
    @Bean(name = {"recruitEntityManagerFactory", "entityManagerFactory"})
    public LocalContainerEntityManagerFactoryBean recruitEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("recruitDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.example.kafka.entity")
                .persistenceUnit("recruit")
                .build();
    }

    @Primary
    @Bean(name = {"recruitTransactionManager", "transactionManager"})
    public PlatformTransactionManager recruitTransactionManager(
            @Qualifier("recruitEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
