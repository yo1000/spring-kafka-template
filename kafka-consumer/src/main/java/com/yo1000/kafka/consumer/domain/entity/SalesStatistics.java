package com.yo1000.kafka.consumer.domain.entity;

import com.yo1000.kafka.consumer.domain.vo.CustomerAttributes;
import com.yo1000.kafka.consumer.domain.vo.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesStatistics {
    @Id
    private String id;
    private LocalDate date;
    private CustomerAttributes customerAttributes;
    private Product product;
    private Integer quantity;
    private Integer amount;

    public SalesStatistics add(SalesStatistics salesStatistics) {
        if (salesStatistics == null) {
            return new SalesStatistics(
                    getId(), getDate(), getCustomerAttributes(),
                    getProduct(), getQuantity(), getAmount());
        }

        if (!getId().equals(salesStatistics.getId())) {
            throw new IllegalArgumentException("SalesStatistics ID mismatch - "
                    + getId() + " : " + salesStatistics.getId());
        }

        if (!getDate().equals(salesStatistics.getDate())) {
            throw new IllegalArgumentException("SalesStatistics date mismatch - "
                    + getDate() + " : " + salesStatistics.getDate());
        }

        if (!getCustomerAttributes().equals(salesStatistics.getCustomerAttributes())) {
            throw new IllegalArgumentException("SalesStatistics customer attributes mismatch - "
                    + getCustomerAttributes() + " : " + salesStatistics.getCustomerAttributes());
        }

        if (!getProduct().getName().equals(salesStatistics.getProduct().getName())) {
            throw new IllegalArgumentException("SalesStatistics product mismatch - "
                    + getProduct() + " : " + salesStatistics.getProduct());
        }

        return add(salesStatistics.getQuantity(), salesStatistics.getAmount());
    }

    public SalesStatistics add(int quantity, int amount) {
        return new SalesStatistics(
                getId(),
                getDate(),
                getCustomerAttributes(),
                getProduct(),
                getQuantity() + quantity,
                getAmount() + amount);
    }
}
