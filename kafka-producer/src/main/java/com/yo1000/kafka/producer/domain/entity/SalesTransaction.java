package com.yo1000.kafka.producer.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yo1000.kafka.producer.domain.vo.CustomerAttributes;
import com.yo1000.kafka.producer.domain.vo.Sales;

import java.time.OffsetDateTime;
import java.util.List;

public record SalesTransaction(
        String id,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        OffsetDateTime timestamp,
        CustomerAttributes customerAttributes,
        List<Sales> sales
) {}
