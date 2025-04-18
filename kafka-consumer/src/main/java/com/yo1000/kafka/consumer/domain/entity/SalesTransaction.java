package com.yo1000.kafka.consumer.domain.entity;

import com.yo1000.kafka.consumer.domain.vo.CustomerAttributes;
import com.yo1000.kafka.consumer.domain.vo.Sales;

import java.time.OffsetDateTime;
import java.util.List;

public record SalesTransaction(
        String id,
        OffsetDateTime timestamp,
        CustomerAttributes customerAttributes,
        List<Sales> sales
) {
    public int total() {
        return sales.stream()
                .mapToInt(sale -> sale.product().unitPrice() * sale.quantity())
                .sum();
    }

    public String products() {
        return sales.stream()
                .map(sale -> sale.product().name() + " x " + sale.quantity())
                .reduce((a, b) -> a + ", " + b)
                .orElseThrow();
    }
}
