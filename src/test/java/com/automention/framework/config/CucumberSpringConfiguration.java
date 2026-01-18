package com.automention.framework.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * Cucumber Spring Configuration
 * Enables Spring context for Cucumber tests
 */
@CucumberContextConfiguration
@SpringBootTest(classes = com.automention.framework.TestAutomationFrameworkApplication.class)
@ContextConfiguration
public class CucumberSpringConfiguration {
    // This class enables Spring dependency injection in Cucumber step definitions
}
