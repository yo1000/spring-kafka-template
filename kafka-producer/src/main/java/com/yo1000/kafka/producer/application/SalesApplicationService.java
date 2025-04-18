package com.yo1000.kafka.producer.application;

import com.yo1000.kafka.producer.domain.entity.SalesTransaction;
import com.yo1000.kafka.producer.domain.vo.*;
import com.yo1000.kafka.producer.util.Chronos;
import com.yo1000.kafka.producer.util.ProductFactors;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.*;

@Service
@Transactional
public class SalesApplicationService {
    private final SecureRandom random = new SecureRandom();
    private final KafkaTemplate<String, SalesTransaction> kafkaTemplate;
    private final Chronos chronos;

    public SalesApplicationService(
            KafkaTemplate<String, SalesTransaction> kafkaTemplate,
            Chronos chronos) {
        this.kafkaTemplate = kafkaTemplate;
        this.chronos = chronos;
    }

    public void sell() {
        randomTransaction().ifPresent(kafkaTemplate::sendDefault);
    }

    private Optional<SalesTransaction> randomTransaction() {
        Genders gender = Genders.lot(random.nextDouble(0.0, 1.0));
        AgeGroups ageGroup = AgeGroups.lot(random.nextDouble(0.0, 1.0));

        List<Sales> sales = ProductFactors.lot(gender,ageGroup).stream()
                .reduce(new HashMap<Product, Integer>(), (map, p) -> {
                    int quantity = Optional.ofNullable(map.get(p)).orElse(0);
                    map.put(p, quantity + 1);
                    return map;
                }, (p1, p2) -> p1)
                .entrySet().stream()
                .map(entry -> new Sales(entry.getKey(), entry.getValue()))
                .toList();

        if (sales.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new SalesTransaction(
                UUID.randomUUID().toString(),
                chronos.nowAsOffsetDateTime(),
                new CustomerAttributes(gender, ageGroup),
                sales));
    }
}
