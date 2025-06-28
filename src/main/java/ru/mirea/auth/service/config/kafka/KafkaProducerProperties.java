package ru.mirea.auth.service.config.kafka;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@Slf4j
@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaProducerProperties {

    private Map<String, ProducerConfig> producers = new HashMap<>();

    @Setter
    @Getter
    public static class ProducerConfig {
        private String keySerializer;
        private String valueSerializer;
        private String acks;
        private boolean enableIdempotence;
        private int retries;
        private int maxInFlightRequestsPerConnection;
        private int batchSize;
        private int lingerMs;
        private int bufferMemory;
        private int deliveryTimeoutMs;  // Новый параметр: максимальное время доставки
        private int requestTimeoutMs;   // Новый параметр: таймаут ожидания ответа от брокера
        private int maxBlockMs;        // Новый параметр: таймаут блокировки
    }
}
