package com.yo1000.kafka.consumer.domain.vo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String name;
    @Transient
    private Integer unitPrice;
}
