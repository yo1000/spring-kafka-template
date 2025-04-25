package com.yo1000.kafka.consumer.presentation;

import com.yo1000.kafka.consumer.application.SalesStatisticsApplicationService;
import com.yo1000.kafka.consumer.domain.entity.SalesTransaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SalesConsumer {
    private final SalesStatisticsApplicationService salesStatisticsApplicationService;

    public SalesConsumer(SalesStatisticsApplicationService salesStatisticsApplicationService) {
        this.salesStatisticsApplicationService = salesStatisticsApplicationService;
    }

    @KafkaListener(topics = "${spring.kafka.template.default-topic}")
    public void listen(
            @Payload SalesTransaction salesTransaction,
            @Header(name = KafkaHeaders.RECEIVED_KEY, required = false) Integer key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.OFFSET) List<Long> offsets,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp,
            Acknowledgment ack) {
        salesStatisticsApplicationService.collectStatistics(salesTransaction);

        ack.acknowledge();
    }
}
