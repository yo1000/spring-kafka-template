package com.yo1000.kafka.consumer.application;

import com.yo1000.kafka.consumer.domain.SalesStatisticsRepository;
import com.yo1000.kafka.consumer.domain.entity.SalesStatistics;
import com.yo1000.kafka.consumer.domain.entity.SalesTransaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SalesStatisticsApplicationService {
    private final SalesStatisticsRepository salesStatisticsRepository;

    public SalesStatisticsApplicationService(SalesStatisticsRepository salesStatisticsRepository) {
        this.salesStatisticsRepository = salesStatisticsRepository;
    }

    public void collectStatistics(SalesTransaction salesTransaction) {
        salesTransaction.toSalesStatisticsList().forEach(newSalesStatistics -> {
            salesStatisticsRepository.findByDateAndCustomerAttributesAndProductName(
                            newSalesStatistics.getDate(),
                            newSalesStatistics.getCustomerAttributes(),
                            newSalesStatistics.getProduct().getName())
                    .ifPresentOrElse(foundSalesStatistics -> {
                        SalesStatistics addedSalesStatistics = foundSalesStatistics.add(newSalesStatistics);
                        salesStatisticsRepository.save(addedSalesStatistics);
                    }, () -> {
                        salesStatisticsRepository.save(newSalesStatistics);
                    });
        });
    }
}
