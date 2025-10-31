package com.example.kafka.config;

import javax.sql.DataSource;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.open_source.repository",
        entityManagerFactoryRef = "openSourceEntityManagerFactory",
        transactionManagerRef = "openSourceTransactionManager"
)
public class OpenSourceDBConfig {

    @Bean
    @ConfigurationProperties(prefix = "open-source.datasource")
    public DataSourceProperties openSourceDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource openSourceDataSource(
            @Qualifier("openSourceDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean openSourceEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("openSourceDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.example.open_source.entity") // entity 패키지
                .persistenceUnit("open-source")
                .build();
    }

    @Bean
    public PlatformTransactionManager openSourceTransactionManager(
            @Qualifier("openSourceEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
