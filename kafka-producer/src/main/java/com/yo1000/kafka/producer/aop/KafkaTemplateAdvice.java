package com.yo1000.kafka.producer.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Aspect
public class KafkaTemplateAdvice {
    @Before("""
        execution(* org.springframework.kafka.core.KafkaTemplate.send(..)) ||
        execution(* org.springframework.kafka.core.KafkaTemplate.sendDefault(..))
        """)
    public void before(JoinPoint joinPoint) {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());

        if (logger.isDebugEnabled()) {
            logger.debug("Send: {}", Arrays.stream(joinPoint.getArgs())
                    .map(o -> Objects.toString(o, "<null>"))
                    .collect(Collectors.joining("; ")));
        }
    }
}
