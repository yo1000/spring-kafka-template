package com.yo1000.kafka.producer.util;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

public class Chronos {
    private final Clock clock;
    private final double timeScale;
    private final OffsetDateTime startTime;

    public Chronos(Clock clock, double timeScale) {
        this.clock = clock;
        this.timeScale = timeScale;
        this.startTime = OffsetDateTime.now(clock);
    }

    public OffsetDateTime now() {
        OffsetDateTime now = OffsetDateTime.now(clock);
        Duration diff = Duration.between(startTime, now);

        long elapsed = (long) (diff.toMillis() / timeScale);
        return startTime.plus(elapsed, ChronoUnit.MILLIS);
    }

    public OffsetDateTime nowAsOffsetDateTime() {
        return now();
    }

    public LocalDateTime nowAsLocalDateTime() {
        return now().toLocalDateTime();
    }

    public long nowAsEpochSeconds() {
        return now().toEpochSecond();
    }
}
