package ru.mirea.auth.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.mirea.auth.service.dto.event.UserCreateEventDto;
import ru.mirea.auth.service.exception.KafkaSerializationException;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRegistrationProducer {

    private final KafkaTemplate<String, String> registrationKafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${topic.user-registration}")
    private String userRegistrationTopic;

    public void sendUserCreatedEvent(UserCreateEventDto eventDto) {
        try {
            String message = objectMapper.writeValueAsString(eventDto);
            registrationKafkaTemplate.send(userRegistrationTopic, eventDto.getExternalId().toString(), message);
            log.info("Sent user registration event to Kafka: {}", message);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize UserCreateEventDto: {}", e.getMessage(), e);
            throw new KafkaSerializationException("Kafka serialization failed");
        }
    }
}

