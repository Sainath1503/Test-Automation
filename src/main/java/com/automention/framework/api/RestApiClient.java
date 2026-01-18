package com.automention.framework.api;

import com.automention.framework.config.ApplicationConfig;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.given;

/**
 * REST API Client using Rest-Assured
 * Handles API requests and responses
 */
@Component
public class RestApiClient {

    private static final Logger logger = LogManager.getLogger(RestApiClient.class);

    @Autowired
    private ApplicationConfig config;

    /**
     * Perform GET request
     */
    public Response performGetRequest(String endpoint) {
        try {
            logger.info("Performing GET request to: {}", endpoint);
            
            Response response = given()
                    .relaxedHTTPSValidation()
                    .when()
                    .get(endpoint)
                    .then()
                    .extract()
                    .response();

            logger.info("Response Status Code: {}", response.getStatusCode());
            logger.info("Response Body: {}", response.getBody().asString());
            
            return response;
        } catch (Exception e) {
            logger.error("Error performing GET request: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Get API response as String
     */
    public String getApiResponseAsString(String endpoint) {
        Response response = performGetRequest(endpoint);
        return response.getBody().asString();
    }

    /**
     * Get products list from API
     */
    public String getProductsList() {
        String apiUrl = config.getApiUrl();
        return getApiResponseAsString(apiUrl);
    }
}
