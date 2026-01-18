package com.automention.framework.stepdefinitions;

import com.automention.framework.api.JsonComparisonUtil;
import com.automention.framework.api.RestApiClient;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Step Definitions for API Test Scenarios
 */
public class ApiStepDefinitions {

    private static final Logger logger = LogManager.getLogger(ApiStepDefinitions.class);

    @Autowired
    private RestApiClient restApiClient;

    @Autowired
    private JsonComparisonUtil jsonComparisonUtil;

    private String apiResponse;
    private String savedResponse;

    @When("I hit the products API endpoint")
    public void iHitTheProductsApiEndpoint() {
        try {
            logger.info("Hitting products API endpoint");
            apiResponse = restApiClient.getProductsList();
            logger.info("API response received");
        } catch (Exception e) {
            logger.error("Error hitting API endpoint: {}", e.getMessage(), e);
            throw e;
        }
    }

    @And("I compare the API response with saved response")
    public void iCompareTheApiResponseWithSavedResponse() {
        try {
            logger.info("Comparing API response with saved response");
            // Load saved response from file (you can store this in resources)
            savedResponse = loadSavedResponse();
            if (savedResponse == null || savedResponse.isEmpty()) {
                logger.warn("Saved response is empty, saving current response as baseline");
                saveResponseAsBaseline(apiResponse);
                savedResponse = apiResponse;
            }
        } catch (Exception e) {
            logger.error("Error comparing API responses: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Then("I should save comparison results to file")
    public void iShouldSaveComparisonResultsToFile() {
        try {
            logger.info("Saving comparison results to file");
            jsonComparisonUtil.compareJsonAndSaveDifferences(savedResponse, apiResponse, "api_comparison");
            logger.info("Comparison results saved successfully");
        } catch (Exception e) {
            logger.error("Error saving comparison results: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Load saved response from file
     */
    private String loadSavedResponse() {
        try {
            Path savedResponsePath = Paths.get("src/test/resources/api/saved_response.json");
            if (Files.exists(savedResponsePath)) {
                return new String(Files.readAllBytes(savedResponsePath));
            }
        } catch (IOException e) {
            logger.warn("Error loading saved response: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Save current response as baseline
     */
    private void saveResponseAsBaseline(String response) {
        try {
            Path baselinePath = Paths.get("src/test/resources/api/saved_response.json");
            Files.createDirectories(baselinePath.getParent());
            Files.write(baselinePath, response.getBytes());
            logger.info("Saved current response as baseline");
        } catch (IOException e) {
            logger.error("Error saving baseline response: {}", e.getMessage(), e);
        }
    }
}
