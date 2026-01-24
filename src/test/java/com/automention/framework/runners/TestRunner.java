package com.automention.framework.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Test Runner for Cucumber Tests
 * Executes BDD test scenarios
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.automention.framework.stepdefinitions",
                "com.automention.framework.config",
                "com.automention.framework.hooks"
        },
        plugin = {
                "pretty",
                "html:target/cucumber-report.html",
                "json:target/cucumber.json",
                "junit:target/cucumber.xml"
        },
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
