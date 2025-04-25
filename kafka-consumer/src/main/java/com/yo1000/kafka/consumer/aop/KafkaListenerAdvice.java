package com.yo1000.kafka.consumer.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Aspect
public class KafkaListenerAdvice {
    @Before("""
        @annotation(org.springframework.kafka.annotation.KafkaListener) &&
        execution(* com.yo1000.kafka.consumer.presentation..*(..))
        """)
    public void before(JoinPoint joinPoint) {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());

        if (!logger.isDebugEnabled()) return;

        if (joinPoint.getSignature() instanceof MethodSignature methodSignature) {
            Parameter[] params = methodSignature.getMethod().getParameters();

            lookupIndex(params, Payload.class).ifPresent(i ->
                    logger.debug("Payload: {}", joinPoint.getArgs()[i]));

            StringBuilder loggingMessage = new StringBuilder();
            List<String> loggingParams = new ArrayList<>();
            lookupIndexes(params, Header.class).forEach(i -> {
                if (loggingMessage.isEmpty()) {
                    loggingMessage.append("Headers: {}={}");
                } else {
                    loggingMessage.append(", {}={}");
                }

                String paramName = params[i].getName();
                if (paramName == null || paramName.isEmpty()) {
                    Header header = params[i].getAnnotation(Header.class);
                    paramName = header.value().isEmpty() ? header.name() : header.value();
                }
                loggingParams.add(paramName);
                loggingParams.add(Optional.ofNullable(joinPoint.getArgs()[i])
                        .orElse("<null>")
                        .toString());
            });

            logger.debug(loggingMessage.toString(), loggingParams.toArray());
        }
    }

    private Optional<Integer> lookupIndex(Parameter[] params, Class<? extends Annotation> annotationClass) {
        for (int i = 0; i < params.length; i++) {
            if (params[i].isAnnotationPresent(annotationClass)) {
                return Optional.of(i);
            }
        }

        return Optional.empty();
    }

    private List<Integer> lookupIndexes(Parameter[] params, Class<? extends Annotation> annotationClass) {
        List<Integer> indexes = new ArrayList<Integer>();

        for (int i = 0; i < params.length; i++) {
            if (params[i].isAnnotationPresent(annotationClass)) {
                indexes.add(i);
            }
        }

        return indexes;
    }
}
