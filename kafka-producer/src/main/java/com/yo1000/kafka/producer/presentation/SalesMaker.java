package com.yo1000.kafka.producer.presentation;

import com.yo1000.kafka.producer.application.SalesApplicationService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@EnableScheduling
public class SalesMaker {
    private final SalesApplicationService salesApplicationService;

    public SalesMaker(SalesApplicationService salesApplicationService) {
        this.salesApplicationService = salesApplicationService;
    }

    @Scheduled(
            fixedDelay = 500L,
            initialDelay = 0L,
            timeUnit = TimeUnit.MILLISECONDS)
    public void makeSales() {
        salesApplicationService.sell();
    }
}
