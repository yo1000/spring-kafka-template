package com.yo1000.kafka.consumer.infrastructure.jpa;

import com.yo1000.kafka.consumer.domain.entity.SalesStatistics;
import com.yo1000.kafka.consumer.domain.vo.AgeGroups;
import com.yo1000.kafka.consumer.domain.vo.CustomerAttributes;
import com.yo1000.kafka.consumer.domain.vo.Genders;
import com.yo1000.kafka.consumer.domain.vo.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;

@DataJpaTest
@Testcontainers
public class JpaSalesStatisticsRepositoryTests {
    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(DockerImageName
            .parse("postgres"));

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> postgresContainer.getJdbcUrl());
        registry.add("spring.datasource.username", () -> postgresContainer.getUsername());
        registry.add("spring.datasource.password", () -> postgresContainer.getPassword());

        // Required to setup DB.
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
        registry.add("spring.jpa.show-sql", () -> true);
    }

    // When using JPA, the TestEntityManager is used to setup test data.
    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    JpaSalesStatisticsRepository salesStatisticsRepository;

    @Test
    void testSave() throws Exception {
        // Given
        SalesStatistics salesStatistics = new SalesStatistics();
        salesStatistics.setId("1");
        salesStatistics.setDate(LocalDate.of(2025, 4, 27));
        salesStatistics.setQuantity(5);
        salesStatistics.setAmount(1_500);
        salesStatistics.setCustomerAttributes(new CustomerAttributes(Genders.FEMALE, AgeGroups.AGE_30_39));
        salesStatistics.setProduct(new Product("Item1", 300));

        // When
        SalesStatistics saved = salesStatisticsRepository.<SalesStatistics>save(salesStatistics);

        // Then
        Assertions.assertThat(saved.getId()).isEqualTo("1");
        Assertions.assertThat(saved.getDate()).isEqualTo(LocalDate.of(2025, 4, 27));
        Assertions.assertThat(saved.getQuantity()).isEqualTo(5);
        Assertions.assertThat(saved.getAmount()).isEqualTo(1_500);
        Assertions.assertThat(saved.getCustomerAttributes())
                .isEqualTo(new CustomerAttributes(Genders.FEMALE, AgeGroups.AGE_30_39));
        Assertions.assertThat(saved.getProduct())
                .isEqualTo(new Product("Item1", 300));

        SalesStatistics actual = testEntityManager.find(SalesStatistics.class, saved.getId());
        Assertions.assertThat(actual.getId()).isEqualTo("1");
        Assertions.assertThat(actual.getDate()).isEqualTo(LocalDate.of(2025, 4, 27));
        Assertions.assertThat(actual.getQuantity()).isEqualTo(5);
        Assertions.assertThat(actual.getAmount()).isEqualTo(1_500);
        Assertions.assertThat(actual.getCustomerAttributes())
                .isEqualTo(new CustomerAttributes(Genders.FEMALE, AgeGroups.AGE_30_39));
        Assertions.assertThat(actual.getProduct())
                .isEqualTo(new Product("Item1", 300));
    }
}
