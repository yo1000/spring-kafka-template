package com.yo1000.kafka.consumer.presentation;

import com.yo1000.kafka.consumer.application.SalesStatisticsApplicationService;
import com.yo1000.kafka.consumer.domain.entity.SalesTransaction;
import com.yo1000.kafka.consumer.domain.vo.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SalesConsumerTests {
    @Container
    static KafkaContainer kafka = new KafkaContainer("apache/kafka-native:3.8.0");
    // Optional Choice.
    // static ConfluentKafkaContainer kafka = new ConfluentKafkaContainer("confluentinc/cp-kafka:7.4.0");

    @Autowired
    KafkaOperations<String, SalesTransaction> kafkaOps;

    @MockitoBean
    SalesStatisticsApplicationService salesStatisticsService;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.kafka.template.default-topic", () -> "test");

        registry.add("spring.kafka.producer.key-serializer", () -> "org.apache.kafka.common.serialization.StringSerializer");
        registry.add("spring.kafka.producer.value-serializer", () -> "org.springframework.kafka.support.serializer.JsonSerializer");
        registry.add("spring.kafka.producer.properties.topic.num.partitions", () -> "1");
        registry.add("spring.kafka.producer.properties.topic.replication.factor", () -> "1");
        registry.add("spring.kafka.producer.properties.spring.json.add.type.headers", () -> "false");
    }

    @Test
    void testListen() throws Exception {
        // Given

        // When verify(..) or capture(..) is used,
        // the Awaitility cannot control the exactly invoking times as expected,
        // so the CountDownLatch is used, which can more control the exactly invoking times.
        CountDownLatch latch = new CountDownLatch(1);

        Mockito.doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(salesStatisticsService).collectStatistics(Mockito.any());

        ArgumentCaptor<SalesTransaction> salesTransactionCaptor =
                ArgumentCaptor.forClass(SalesTransaction.class);

        // When

        kafkaOps.sendDefault(new SalesTransaction(
                "1",
                OffsetDateTime.of(
                        2025, 4, 25,
                        12, 34, 56, 789,
                        ZoneOffset.UTC),
                new CustomerAttributes(
                        Genders.MALE,
                        AgeGroups.AGE_30_39),
                List.of(new Sales(new Product("Item1", 100), 2),
                        new Sales(new Product("Item2", 300), 4))));

        // Then

        if (!latch.await(5_000L, TimeUnit.MILLISECONDS)) {
            Assertions.fail("Latch timed out");
            return;
        }

        Mockito.verify(salesStatisticsService, Mockito.times(1))
                .collectStatistics(salesTransactionCaptor.capture());

        SalesTransaction salesTransaction = salesTransactionCaptor.getValue();

        Assertions.assertThat(salesTransaction.id()).isEqualTo("1");
        Assertions.assertThat(salesTransaction.timestamp())
                .isEqualTo(OffsetDateTime.of(
                        2025, 4, 25,
                        12, 34, 56, 789,
                        ZoneOffset.UTC));
        Assertions.assertThat(salesTransaction.customerAttributes())
                .isEqualTo(new CustomerAttributes(
                        Genders.MALE,
                        AgeGroups.AGE_30_39));
        Assertions.assertThat(salesTransaction.sales())
                .containsExactlyInAnyOrder(
                        new Sales(new Product("Item1", 100), 2),
                        new Sales(new Product("Item2", 300), 4));
    }

    @Test
    void testListen_onException() throws Exception {
        // Given

        // When verify(..) or capture(..) is used,
        // the Awaitility cannot control the exactly invoking times as expected,
        // so the CountDownLatch is used, which can more control the exactly invoking times.
        CountDownLatch latch = new CountDownLatch(2);

        Mockito.doAnswer(invocation -> {
            latch.countDown();
            throw new RuntimeException("for test");
        }).when(salesStatisticsService).collectStatistics(Mockito.any());

        ArgumentCaptor<SalesTransaction> salesTransactionCaptor =
                ArgumentCaptor.forClass(SalesTransaction.class);

        // When

        kafkaOps.sendDefault(new SalesTransaction(
                "1",
                OffsetDateTime.of(
                        2025, 4, 26,
                        12, 34, 56, 789,
                        ZoneOffset.UTC),
                new CustomerAttributes(
                        Genders.MALE,
                        AgeGroups.AGE_30_39),
                List.of(new Sales(new Product("Item1", 100), 2),
                        new Sales(new Product("Item2", 300), 4))));

        // Then

        if (!latch.await(5_000L, TimeUnit.MILLISECONDS)) {
            Assertions.fail("Latch timed out");
            return;
        }

        // Listen to the same record repeatedly because the offset is not committed.
        Mockito.verify(salesStatisticsService, Mockito.atLeast(2))
                .collectStatistics(salesTransactionCaptor.capture());

        SalesTransaction salesTransaction = salesTransactionCaptor.getValue();

        Assertions.assertThat(salesTransaction.id()).isEqualTo("1");
        Assertions.assertThat(salesTransaction.timestamp())
                .isEqualTo(OffsetDateTime.of(
                        2025, 4, 26,
                        12, 34, 56, 789,
                        ZoneOffset.UTC));
        Assertions.assertThat(salesTransaction.customerAttributes())
                .isEqualTo(new CustomerAttributes(
                        Genders.MALE,
                        AgeGroups.AGE_30_39));
        Assertions.assertThat(salesTransaction.sales())
                .containsExactlyInAnyOrder(
                        new Sales(new Product("Item1", 100), 2),
                        new Sales(new Product("Item2", 300), 4));
    }
}
