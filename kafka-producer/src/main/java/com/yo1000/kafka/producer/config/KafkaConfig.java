package com.yo1000.kafka.producer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic topic(KafkaProperties kafkaProperties) {
        return new NewTopic(
                kafkaProperties.getTemplate().getDefaultTopic(),
                Optional.empty(),
                Optional.empty());
    }
}
