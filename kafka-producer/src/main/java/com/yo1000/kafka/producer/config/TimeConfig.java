package com.yo1000.kafka.producer.config;

import com.yo1000.kafka.producer.util.Chronos;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableConfigurationProperties(TimeProperties.class)
public class TimeConfig {
    @Bean
    public Clock clock(TimeProperties timeProperties) {
        Clock baseClock = Clock.systemDefaultZone();

        Duration diff = Duration.between(
                OffsetDateTime.now(baseClock),
                OffsetDateTime.parse(
                        timeProperties.getInitial(),
                        DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSZ")));

        return Clock.offset(baseClock, diff);
    }

    @Bean
    public Chronos chronos(TimeProperties timeProperties, Clock clock) {
        return new Chronos(clock, timeProperties.getxSpeed());
    }
}
