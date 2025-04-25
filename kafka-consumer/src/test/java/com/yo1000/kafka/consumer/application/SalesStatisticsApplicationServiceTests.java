package com.yo1000.kafka.consumer.application;

import com.yo1000.kafka.consumer.domain.SalesStatisticsRepository;
import com.yo1000.kafka.consumer.domain.entity.SalesStatistics;
import com.yo1000.kafka.consumer.domain.entity.SalesTransaction;
import com.yo1000.kafka.consumer.domain.vo.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class SalesStatisticsApplicationServiceTests {
    @Test
    public void testCollectStatistics() throws Exception {
        // Given

        SalesStatisticsRepository salesStatisticsRepository =
                Mockito.mock(SalesStatisticsRepository.class);

        SalesStatisticsApplicationService salesStatisticsApplicationService =
                new SalesStatisticsApplicationService(salesStatisticsRepository);

        ArgumentCaptor<SalesStatistics> salesStatisticsCaptor =
                ArgumentCaptor.forClass(SalesStatistics.class);

        // When

        salesStatisticsApplicationService.collectStatistics(new SalesTransaction(
                "1",
                OffsetDateTime.of(
                        2025, 4, 24,
                        15, 26, 37, 489,
                        ZoneOffset.UTC),
                new CustomerAttributes(
                        Genders.FEMALE,
                        AgeGroups.AGE_20_29),
                List.of(new Sales(new Product("Item1", 1_000), 1),
                        new Sales(new Product("Item2", 2_000), 2))));

        // Then
        
        Mockito.verify(salesStatisticsRepository, Mockito.times(2))
                .save(salesStatisticsCaptor.capture());

        List<SalesStatistics> allSalesStatistics = salesStatisticsCaptor.getAllValues();

        Assertions.assertThat(allSalesStatistics.get(0).getId()).isNotBlank();
        Assertions.assertThat(allSalesStatistics.get(0).getDate()).isEqualTo(LocalDate.of(2025, 4, 24));
        Assertions.assertThat(allSalesStatistics.get(0).getQuantity()).isEqualTo(1);
        Assertions.assertThat(allSalesStatistics.get(0).getAmount()).isEqualTo(1_000);
        Assertions.assertThat(allSalesStatistics.get(0).getCustomerAttributes())
                .isEqualTo(new CustomerAttributes(Genders.FEMALE, AgeGroups.AGE_20_29));
        Assertions.assertThat(allSalesStatistics.get(0).getProduct())
                .isEqualTo(new Product("Item1", 1_000));

        Assertions.assertThat(allSalesStatistics.get(1).getId()).isNotBlank();
        Assertions.assertThat(allSalesStatistics.get(1).getDate()).isEqualTo(LocalDate.of(2025, 4, 24));
        Assertions.assertThat(allSalesStatistics.get(1).getQuantity()).isEqualTo(2);
        Assertions.assertThat(allSalesStatistics.get(1).getAmount()).isEqualTo(4_000);
        Assertions.assertThat(allSalesStatistics.get(1).getCustomerAttributes())
                .isEqualTo(new CustomerAttributes(Genders.FEMALE, AgeGroups.AGE_20_29));
        Assertions.assertThat(allSalesStatistics.get(1).getProduct())
                .isEqualTo(new Product("Item2", 2_000));
    }
}
