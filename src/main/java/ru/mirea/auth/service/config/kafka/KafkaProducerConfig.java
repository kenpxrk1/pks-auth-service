package ru.mirea.auth.service.config.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaProducerProperties.class)
public class KafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    private final KafkaProducerProperties producerProperties;

    @Bean
    public ProducerFactory<String, String> registrationProducerFactory() {
        KafkaProducerProperties.ProducerConfig config =
                producerProperties.getProducers().get("registration");
        return new DefaultKafkaProducerFactory<>(buildConfig(config));
    }

    @Bean
    public KafkaTemplate<String, String> registrationKafkaTemplate() {
        return new KafkaTemplate<>(registrationProducerFactory());
    }

    private Map<String, Object> buildConfig(KafkaProducerProperties.ProducerConfig config) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, config.getKeySerializer());
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, config.getValueSerializer());
        configProps.put(ProducerConfig.ACKS_CONFIG, config.getAcks());
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, config.isEnableIdempotence());
        configProps.put(ProducerConfig.RETRIES_CONFIG, config.getRetries());
        configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,
                config.getMaxInFlightRequestsPerConnection());
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, config.getBatchSize());
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, config.getLingerMs());
        configProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, config.getBufferMemory());
        configProps.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, config.getDeliveryTimeoutMs());
        configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, config.getRequestTimeoutMs());
        configProps.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, config.getMaxBlockMs());

        return configProps;
    }
}
