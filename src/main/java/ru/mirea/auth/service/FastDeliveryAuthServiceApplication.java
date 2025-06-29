package ru.mirea.auth.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
public class FastDeliveryAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FastDeliveryAuthServiceApplication.class, args);
	}

}
