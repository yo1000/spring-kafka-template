package com.yo1000.kafka.producer.domain.vo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class AgeGroupsTests {
    @Test
    public void testSummaryRatioShouldBeEq1() throws Throwable {
        Assertions.assertThat(Arrays.stream(AgeGroups.values())
                        .map(AgeGroups::getRatio)
                        .reduce(Double::sum)
                        .get())
                .isEqualTo(1.0);
    }
}
