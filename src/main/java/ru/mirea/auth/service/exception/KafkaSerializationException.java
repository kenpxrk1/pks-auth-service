package ru.mirea.auth.service.exception;

public class KafkaSerializationException extends RuntimeException {
    public KafkaSerializationException(String message) {
        super(message);
    }
}
