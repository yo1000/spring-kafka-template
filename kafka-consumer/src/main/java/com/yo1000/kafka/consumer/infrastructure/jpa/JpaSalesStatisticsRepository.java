package com.yo1000.kafka.consumer.infrastructure.jpa;

import com.yo1000.kafka.consumer.domain.SalesStatisticsRepository;
import com.yo1000.kafka.consumer.domain.entity.SalesStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSalesStatisticsRepository
        extends SalesStatisticsRepository, JpaRepository<SalesStatistics, String> {
    @Override
    SalesStatistics save(SalesStatistics salesStatistics);
}
