package com.example.open_source.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.repository",
        entityManagerFactoryRef = "jpaEntityManagerFactory",
        transactionManagerRef = "jpaTransactionManager"
)
public class DBConfig {

    // 1. DataSource (접속 정보는 메인 모듈의 application.yml에서 주입받음)
    // prefix를 jpa.datasource로 명확히 분리하여 다른 DB와 충돌을 방지합니다.
    @Bean(name = "jpaDataSource")
    @ConfigurationProperties(prefix = "jpa.datasource")
    public DataSource jpaDataSource() {
        return DataSourceBuilder.create().build();
    }

    // 2. EntityManagerFactory (Entity와 DB 연결을 관리)
    @Bean(name = "jpaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean jpaEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(jpaDataSource());
        factory.setPackagesToScan("com.example.jpa.entity"); // 이 모듈의 Entity 패키지
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return factory;
    }

    // 3. TransactionManager (트랜잭션 관리)
    @Bean(name = "jpaTransactionManager")
    public PlatformTransactionManager jpaTransactionManager() {
        return new JpaTransactionManager(jpaEntityManagerFactory().getObject());
    }
}
