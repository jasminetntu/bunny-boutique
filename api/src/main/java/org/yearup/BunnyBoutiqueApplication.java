package org.yearup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Bunny Boutique Spring Boot application.
 * <p>
 * Bootstraps the application using Spring Boot's auto-configuration & component scanning.
 * </p>
 */
@SpringBootApplication
public class BunnyBoutiqueApplication {
    public static void main(String[] args) {
        SpringApplication.run(BunnyBoutiqueApplication.class, args);
    }
}
