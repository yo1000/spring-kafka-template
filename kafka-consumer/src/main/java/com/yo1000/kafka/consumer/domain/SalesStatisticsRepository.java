package com.yo1000.kafka.consumer.domain;

import com.yo1000.kafka.consumer.domain.entity.SalesStatistics;
import com.yo1000.kafka.consumer.domain.vo.CustomerAttributes;

import java.time.LocalDate;
import java.util.Optional;

public interface SalesStatisticsRepository {
    SalesStatistics save(SalesStatistics salesStatistics);

    Optional<SalesStatistics> findByDateAndCustomerAttributesAndProductName(
            LocalDate date,
            CustomerAttributes customerAttributes,
            String productName
    );
}
