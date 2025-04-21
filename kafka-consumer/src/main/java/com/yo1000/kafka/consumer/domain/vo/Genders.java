package com.yo1000.kafka.consumer.domain.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Objects;

public enum Genders {
    MALE("M", 0.48),
    FEMALE("F", 0.51),
    UNKNOWN("U", 0.01),
    ;

    private final String value;
    private final double ratio;

    Genders(String value, double ratio) {
        this.value = value;
        this.ratio = ratio;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public double getRatio() {
        return ratio;
    }

    @JsonCreator
    public static Genders of(String value) {
        return Arrays.stream(values())
                .filter(g -> Objects.nonNull(g.value))
                .filter(g -> g.value.equalsIgnoreCase(value))
                .findFirst()
                .orElse(UNKNOWN);
    }

    public static Genders lot(double v) {
        if (v > 1.0) throw new IllegalArgumentException("v > 1.0");

        double sum = 0.0;
        for (Genders g : Genders.values()) {
            sum += g.getRatio();
            if (v < sum) return g;
        }

        return UNKNOWN;
    }
}
