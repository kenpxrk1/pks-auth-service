package ru.mirea.auth.service.exception;

public class KafkaPublishException extends RuntimeException {
    public KafkaPublishException(String message) {
        super(message);
    }
}
