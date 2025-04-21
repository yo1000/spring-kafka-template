package com.yo1000.kafka.consumer.domain.entity;

import com.yo1000.kafka.consumer.domain.vo.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

public record SalesTransaction(
        String id,
        OffsetDateTime timestamp,
        CustomerAttributes customerAttributes,
        List<Sales> sales
) {
    public int total() {
        return sales.stream()
                .mapToInt(sale -> sale.product().getUnitPrice() * sale.quantity())
                .sum();
    }

    public String products() {
        return sales.stream()
                .map(sale -> sale.product().getName() + " x " + sale.quantity())
                .reduce((a, b) -> a + ", " + b)
                .orElseThrow();
    }

    public List<SalesStatistics> toSalesStatisticsList() {
        LocalDate date = timestamp().toLocalDate();

        return sales().stream()
                .reduce(new HashMap<Product, Integer>(), (prodQty, sale) -> {
                    int qty = prodQty.getOrDefault(sale.product(), 0) + sale.quantity();
                    prodQty.put(sale.product(), qty);
                    return prodQty;
                }, (a, b) -> a)
                .entrySet().stream()
                .map(prodQty -> new SalesStatistics(
                        generateId(
                                prodQty.getKey().getName(),
                                date,
                                customerAttributes().getAgeGroup(),
                                customerAttributes().getGenders()),
                        date,
                        customerAttributes(),
                        prodQty.getKey(),
                        prodQty.getValue(),
                        prodQty.getKey().getUnitPrice() * prodQty.getValue()
                ))
                .toList();
    }

    private String generateId(String name, LocalDate date, AgeGroups ageGroups, Genders gender) {
        String input = name + ";" + date + ";" + ageGroups + ";" + gender;
        return generateId(input);

    }

    private String generateId(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
