package com.automention.framework.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * JSON Comparison Utility using SkyScreamer
 * Compares JSON responses and identifies differences
 */
@Component
public class JsonComparisonUtil {

    private static final Logger logger = LogManager.getLogger(JsonComparisonUtil.class);
    private static final String COMPARISON_OUTPUT_DIR = "target/api-comparison/";

    /**
     * Compare two JSON strings and save differences to file
     */
    public void compareJsonAndSaveDifferences(String expectedJson, String actualJson, String fileName) {
        try {
            // Create output directory
            Path outputPath = Paths.get(COMPARISON_OUTPUT_DIR);
            if (!Files.exists(outputPath)) {
                Files.createDirectories(outputPath);
            }

            // Generate filename with timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String outputFileName = fileName + "_" + timestamp + ".txt";
            Path filePath = outputPath.resolve(outputFileName);

            // Try to compare and capture result
            boolean comparisonPassed = true;
            String comparisonMessage = "";

            try {
                JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
                comparisonMessage = "JSONs are identical";
            } catch (AssertionError e) {
                comparisonPassed = false;
                comparisonMessage = e.getMessage();
            }

            // Write comparison results to file
            try (FileWriter writer = new FileWriter(filePath.toFile())) {
                writer.write("JSON Comparison Report\n");
                writer.write("=================================\n\n");
                writer.write("Timestamp: " + LocalDateTime.now() + "\n\n");
                
                if (comparisonPassed) {
                    writer.write("Status: PASSED - " + comparisonMessage + "\n");
                } else {
                    writer.write("Status: FAILED - Differences found\n\n");
                    writer.write("Comparison Message:\n");
                    writer.write(comparisonMessage + "\n\n");
                }
                
                writer.write("\nExpected JSON:\n");
                writer.write(expectedJson + "\n\n");
                writer.write("Actual JSON:\n");
                writer.write(actualJson + "\n");
            }

            logger.info("Comparison results saved to: {}", filePath.toAbsolutePath());
            
            if (!comparisonPassed) {
                logger.warn("JSON comparison failed. Message: {}", comparisonMessage);
            }

        } catch (Exception e) {
            logger.error("Error comparing JSON: {}", e.getMessage(), e);
            throw new RuntimeException("JSON comparison failed", e);
        }
    }

    /**
     * Compare JSON with lenient mode (ignores extra fields)
     */
    public boolean compareJsonLenient(String expectedJson, String actualJson) {
        try {
            JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.LENIENT);
            logger.info("JSON comparison passed (lenient mode)");
            return true;
        } catch (AssertionError e) {
            logger.warn("JSON comparison failed (lenient mode): {}", e.getMessage());
            return false;
        } catch (org.json.JSONException e) {
            logger.error("JSON parsing error (lenient mode): {}", e.getMessage());
            return false;
        }
    }

    /**
     * Compare JSON with strict mode
     */
    public boolean compareJsonStrict(String expectedJson, String actualJson) {
        try {
            JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
            logger.info("JSON comparison passed (strict mode)");
            return true;
        } catch (AssertionError e) {
            logger.warn("JSON comparison failed (strict mode): {}", e.getMessage());
            return false;
        } catch (org.json.JSONException e) {
            logger.error("JSON parsing error (strict mode): {}", e.getMessage());
            return false;
        }
    }
}
