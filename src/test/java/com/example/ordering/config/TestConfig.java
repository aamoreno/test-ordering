package com.example.ordering.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration class to ensure test environment uses H2 in-memory database
 * while the main application uses MySQL.
 */
@Configuration
@Profile("test")
@PropertySource("classpath:application-test.properties")
public class TestConfig {
    // No bean definitions needed - just the configuration annotations
}
