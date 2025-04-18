package com.yo1000.kafka.producer.util;

import com.yo1000.kafka.producer.domain.vo.AgeGroups;
import com.yo1000.kafka.producer.domain.vo.Genders;
import com.yo1000.kafka.producer.domain.vo.Product;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;

public class ProductFactors {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static List<Product> lot(Genders genders, AgeGroups ageGroups) {
        Stream<Product> products = Stream.of();

        int challenge = RANDOM.nextInt(0, 5);

        for (int i = 0; i < challenge; i++) {
            Stream<Product> p = table.entrySet().stream()
                    .filter(entry -> RANDOM.nextDouble(0, 1) < entry.getValue().resolve(genders, ageGroups))
                    .map(Map.Entry::getKey);

            products = Stream.concat(products, p);
        }

        return products.toList();
    }

    private static final Map<Product, Factors> table = ofEntries(
            entry(new Product("Potion", 100),
                    new Factors(0.002, 0.003, 0.005, 0.050, 0.007, 0.005, 0.002, 0.005, 0.006, 0.008, 0.006, 0.003)),
            entry(new Product("Hi-Potion", 500),
                    new Factors(0.003, 0.002, 0.001, 0.020, 0.008, 0.011, 0.020, 0.015, 0.013, 0.010, 0.008, 0.002)),
            entry(new Product("X-Potion", 5_000),
                    new Factors(0.003, 0.002, 0.000, 0.005, 0.002, 0.005, 0.002, 0.007, 0.006, 0.004, 0.003, 0.001)),
            entry(new Product("Mega-Potion", 10_000),
                    new Factors(0.003, 0.002, 0.000, 0.000, 0.004, 0.004, 0.002, 0.001, 0.001, 0.001, 0.000, 0.000)),
            entry(new Product("Phoenix Down", 500),
                    new Factors(0.002, 0.002, 0.000, 0.010, 0.010, 0.010, 0.012, 0.008, 0.007, 0.005, 0.004, 0.002)),
            entry(new Product("Mega Phoenix", 10_000),
                    new Factors(0.003, 0.002, 0.000, 0.000, 0.001, 0.003, 0.005, 0.001, 0.001, 0.001, 0.000, 0.000)),
            entry(new Product("Elixir", 50_000),
                    new Factors(0.002, 0.002, 0.000, 0.001, 0.002, 0.004, 0.003, 0.002, 0.002, 0.001, 0.001, 0.000)),
            entry(new Product("Antidote", 100),
                    new Factors(0.002, 0.003, 0.001, 0.010, 0.003, 0.002, 0.003, 0.002, 0.002, 0.002, 0.001, 0.001)),
            entry(new Product("Soft", 100),
                    new Factors(0.002, 0.003, 0.000, 0.010, 0.003, 0.002, 0.004, 0.003, 0.002, 0.002, 0.001, 0.001)),
            entry(new Product("Eye Drops", 100),
                    new Factors(0.002, 0.003, 0.001, 0.010, 0.002, 0.001, 0.004, 0.003, 0.002, 0.002, 0.001, 0.001)),
            entry(new Product("Echo Screen", 100),
                    new Factors(0.002, 0.003, 0.000, 0.010, 0.004, 0.003, 0.006, 0.003, 0.002, 0.002, 0.001, 0.001)),
            entry(new Product("Holy Water", 100),
                    new Factors(0.002, 0.003, 0.000, 0.002, 0.002, 0.001, 0.005, 0.004, 0.004, 0.002, 0.002, 0.001)),
            entry(new Product("Remedy", 1_000),
                    new Factors(0.003, 0.002, 0.000, 0.005, 0.008, 0.010, 0.008, 0.005, 0.006, 0.003, 0.002, 0.001))
    );

    private record Factors(
            double male,
            double female,
            double age0,
            double age10,
            double age20,
            double age30,
            double age40,
            double age50,
            double age60,
            double age70,
            double age80,
            double age90
    ) {
        public double resolve(Genders genders, AgeGroups ageGroups) {
            double sum = 0.0;

            if (genders == Genders.MALE) sum += male;
            if (genders == Genders.FEMALE) sum += female;

            if (ageGroups == AgeGroups.AGE_0_9) sum += age0;
            if (ageGroups == AgeGroups.AGE_10_19) sum += age10;
            if (ageGroups == AgeGroups.AGE_20_29) sum += age20;
            if (ageGroups == AgeGroups.AGE_30_39) sum += age30;
            if (ageGroups == AgeGroups.AGE_40_49) sum += age40;
            if (ageGroups == AgeGroups.AGE_50_59) sum += age50;
            if (ageGroups == AgeGroups.AGE_60_69) sum += age60;
            if (ageGroups == AgeGroups.AGE_70_79) sum += age70;
            if (ageGroups == AgeGroups.AGE_80_89) sum += age80;
            if (ageGroups == AgeGroups.AGE_90_OR_MORE) sum += age90;

            return sum;
        }
    }
}
