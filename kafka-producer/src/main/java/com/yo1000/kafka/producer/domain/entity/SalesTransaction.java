package com.yo1000.kafka.producer.domain.entity;

import com.yo1000.kafka.producer.domain.vo.CustomerAttributes;
import com.yo1000.kafka.producer.domain.vo.Sales;

import java.time.OffsetDateTime;
import java.util.List;

public record SalesTransaction(
        String id,
        OffsetDateTime timestamp,
        CustomerAttributes customerAttributes,
        List<Sales> sales
) {}
