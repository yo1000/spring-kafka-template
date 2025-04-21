package com.yo1000.kafka.consumer.domain.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum AgeGroups {
    AGE_0_9(0, 0.02),
    AGE_10_19(10, 0.14),
    AGE_20_29(20, 0.10),
    AGE_30_39(30, 0.11),
    AGE_40_49(40, 0.14),
    AGE_50_59(50, 0.21),
    AGE_60_69(60, 0.18),
    AGE_70_79(70, 0.06),
    AGE_80_89(80, 0.03),
    AGE_90_OR_MORE(90, 0.01),
    UNKNOWN(-1, 0),
    ;

    private final int value;
    private final double ratio;

    AgeGroups(int value, double ratio) {
        this.value = value;
        this.ratio = ratio;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    public double getRatio() {
        return ratio;
    }

    @JsonCreator
    public static AgeGroups of(int value) {
        return Arrays.stream(values())
                .filter(group -> group.value == value)
                .findFirst()
                .orElse(UNKNOWN);
    }

    public static AgeGroups lot(double v) {
        if (v > 1.0) throw new IllegalArgumentException("v > 1.0");

        double sum = 0.0;
        for (AgeGroups g : AgeGroups.values()) {
            sum += g.getRatio();
            if (v < sum) return g;
        }

        return UNKNOWN;
    }
}
