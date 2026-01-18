package com.automention.framework.stepdefinitions;

import com.automention.framework.service.DatabaseService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Step Definitions for Database Test Scenarios
 */
public class DatabaseStepDefinitions {

    private static final Logger logger = LogManager.getLogger(DatabaseStepDefinitions.class);

    @Autowired
    private DatabaseService databaseService;

    @When("I connect to the database")
    public void iConnectToTheDatabase() {
        try {
            logger.info("Connecting to database");
            // Connection is managed by Spring/Hibernate
            logger.info("Database connection established");
        } catch (Exception e) {
            logger.error("Error connecting to database: {}", e.getMessage(), e);
            throw e;
        }
    }

    @And("I retrieve all products from database")
    public void iRetrieveAllProductsFromDatabase() {
        try {
            logger.info("Retrieving all products from database");
            databaseService.getAllProducts();
            logger.info("Products retrieved successfully");
        } catch (Exception e) {
            logger.error("Error retrieving products: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Then("I should print products in console")
    public void iShouldPrintProductsInConsole() {
        try {
            logger.info("Printing products to console");
            databaseService.printAllProducts();
            logger.info("Products printed to console successfully");
        } catch (Exception e) {
            logger.error("Error printing products: {}", e.getMessage(), e);
            throw e;
        }
    }
}
