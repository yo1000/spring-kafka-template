package com.yo1000.kafka.producer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
// import org.testcontainers.kafka.ConfluentKafkaContainer;

@SpringBootTest
@Testcontainers
class KafkaProducerApplicationTests {
	@Container
    static KafkaContainer kafka = new KafkaContainer("apache/kafka-native:3.8.0");
	// Optional Choice.
	//static ConfluentKafkaContainer kafka = new ConfluentKafkaContainer("confluentinc/cp-kafka:7.4.0");

	@DynamicPropertySource
	static void registerProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
		registry.add("spring.kafka.template.default-topic", () -> "test");
	}

	@Test
	void contextLoads() {}
}
