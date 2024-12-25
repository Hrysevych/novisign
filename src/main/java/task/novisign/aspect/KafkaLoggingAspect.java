package task.novisign.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class KafkaLoggingAspect {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final String KAFKA_TOPIC = "novisign";

    @Autowired
    public KafkaLoggingAspect(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Around("@annotation(LogToKafka)")
    public Object logToKafka(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        String message = "Method: " + joinPoint.getSignature().getName() +
                " Arguments: " + Arrays.toString(joinPoint.getArgs()) +
                " Result: " + result;

        kafkaTemplate.send(KAFKA_TOPIC, message);
        return result;
    }
}
