package com.automention.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main Spring Boot Application Class
 * Entry point for the Test Automation Framework
 */
@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchClientAutoConfiguration.class})
@ComponentScan(basePackages = "com.automention.framework")
public class TestAutomationFrameworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestAutomationFrameworkApplication.class, args);
    }
}
