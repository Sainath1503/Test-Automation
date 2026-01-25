package com.automention.framework.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ISuiteListener;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * TestNG Suite Listener to print report summary at the end of test execution
 * Displays Cucumber report location and Elasticsearch/Kibana URLs
 */
public class ReportSummaryListener implements ISuiteListener {

    private static final Logger logger = LogManager.getLogger(ReportSummaryListener.class);
    private Properties properties;

    public ReportSummaryListener() {
        loadProperties();
    }

    private void loadProperties() {
        properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (Exception e) {
            logger.warn("Could not load application.properties: {}", e.getMessage());
        }
    }

    @Override
    public void onStart(ISuite suite) {
        // No action needed at start
    }

    @Override
    public void onFinish(ISuite suite) {
        // Check for test failures
        boolean hasFailures = checkForTestFailures(suite);
        
        System.out.println("");
        System.out.println("=================================================================================");
        
        // Print test execution status
        if (hasFailures) {
            System.out.println("                              ⚠️  TEST FAILURE ⚠️                              ");
            System.out.println("=================================================================================");
            System.out.println("");
            System.out.println("One or more tests have failed. Please review the test results below.");
            System.out.println("");
        } else {
            System.out.println("                    TEST EXECUTION SUMMARY - REPORT LOCATIONS                    ");
        }
        System.out.println("=================================================================================");
        System.out.println("");
        
        // Print test statistics
        printTestStatistics(suite, hasFailures);
        
        // Print Cucumber HTML Report Location
        printCucumberReportLocation();
        
        // Print Elasticsearch and Kibana URLs
        printElasticsearchKibanaInfo();
        
        System.out.println("=================================================================================");
        if (hasFailures) {
            System.out.println("                              ⚠️  TEST FAILURE ⚠️                              ");
        }
        System.out.println("=================================================================================");
        System.out.println("");
    }
    
    /**
     * Check if there are any test failures in the suite
     */
    private boolean checkForTestFailures(ISuite suite) {
        try {
            for (ISuiteResult suiteResult : suite.getResults().values()) {
                ITestContext testContext = suiteResult.getTestContext();
                int failedTests = testContext.getFailedTests().size();
                int failedConfigurations = testContext.getFailedConfigurations().size();
                
                if (failedTests > 0 || failedConfigurations > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            logger.warn("Error checking for test failures: {}", e.getMessage());
        }
        return false;
    }
    
    /**
     * Print test execution statistics
     */
    private void printTestStatistics(ISuite suite, boolean hasFailures) {
        try {
            int passedTests = 0;
            int failedTests = 0;
            int skippedTests = 0;
            
            for (ISuiteResult suiteResult : suite.getResults().values()) {
                ITestContext testContext = suiteResult.getTestContext();
                passedTests += testContext.getPassedTests().size();
                failedTests += testContext.getFailedTests().size();
                skippedTests += testContext.getSkippedTests().size();
            }
            
            // Calculate total from actual results (more accurate than getAllTestMethods)
            int totalTests = passedTests + failedTests + skippedTests;
            
            System.out.println("TEST EXECUTION STATISTICS:");
            System.out.println("  Total Tests:  " + totalTests);
            System.out.println("  Passed:       " + passedTests + " ✓");
            System.out.println("  Failed:       " + failedTests + (hasFailures ? " ✗" : ""));
            System.out.println("  Skipped:      " + skippedTests);
            System.out.println("");
            
            if (hasFailures) {
                System.out.println("STATUS: TEST FAILURE - " + failedTests + " test(s) failed");
            } else {
                System.out.println("STATUS: ALL TESTS PASSED");
            }
            System.out.println("");
        } catch (Exception e) {
            logger.warn("Error printing test statistics: {}", e.getMessage());
        }
    }

    private void printCucumberReportLocation() {
        try {
            String targetDir = "target";
            String reportFileName = "cucumber-report.html";
            String reportPath = Paths.get(targetDir, reportFileName).toString();
            
            File reportFile = new File(reportPath);
            String absolutePath = reportFile.getAbsolutePath();
            String reportUrl = "file:///" + absolutePath.replace("\\", "/");
            
            if (reportFile.exists()) {
                System.out.println("CUCUMBER HTML TEST REPORT:");
                System.out.println("  Report Type: HTML Format");
                System.out.println("  File Name:   " + reportFileName);
                System.out.println("  Full Path:   " + absolutePath);
                System.out.println("  Open in Browser: " + reportUrl);
                System.out.println("  Note:        Double-click the file to open in your default browser");
            } else {
                System.out.println("CUCUMBER HTML TEST REPORT:");
                System.out.println("  File Name:   " + reportFileName);
                System.out.println("  Full Path:   " + absolutePath);
                System.out.println("  Status:      Will be generated after test completion");
            }
            System.out.println("");
        } catch (Exception e) {
            System.out.println("WARNING: Error determining report location: " + e.getMessage());
            System.out.println("");
        }
    }

    private void printElasticsearchKibanaInfo() {
        String elasticsearchHost = properties.getProperty("elasticsearch.host", "localhost");
        String elasticsearchPort = properties.getProperty("elasticsearch.port", "9200");
        String elasticsearchEnabled = properties.getProperty("elasticsearch.enabled", "false");
        
        boolean isEnabled = Boolean.parseBoolean(elasticsearchEnabled);
        
        System.out.println("ELASTICSEARCH CONFIGURATION:");
        System.out.println("  Status:     " + (isEnabled ? "ENABLED" : "DISABLED"));
        System.out.println("  Host:       " + elasticsearchHost);
        System.out.println("  Port:       " + elasticsearchPort);
        if (isEnabled) {
            String elasticsearchUrl = "http://" + elasticsearchHost + ":" + elasticsearchPort;
            System.out.println("  URL:        " + elasticsearchUrl);
            System.out.println("  Health Check: " + elasticsearchUrl + "/_cluster/health");
        } else {
            System.out.println("  URL:        http://" + elasticsearchHost + ":" + elasticsearchPort + " (when enabled)");
            System.out.println("  Note:       Set elasticsearch.enabled=true in application.properties to enable");
        }
        System.out.println("");
        
        System.out.println("KIBANA CONFIGURATION:");
        if (isEnabled) {
            String kibanaUrl = "http://localhost:5933";
            System.out.println("  Status:     ENABLED (if Docker container is running)");
            System.out.println("  URL:        " + kibanaUrl);
            System.out.println("  Port:       5933 (mapped from container port 5601)");
            System.out.println("  Note:       Kibana port is mapped to 5933 in docker-compose.yml for Windows");
        } else {
            System.out.println("  Status:     NOT ACCESSIBLE (Elasticsearch is disabled)");
            System.out.println("  URL:        http://localhost:5933 (when Elasticsearch is enabled)");
            System.out.println("  Port:       5933");
            System.out.println("  Note:       Enable Elasticsearch first, then start Kibana container");
        }
        System.out.println("");
    }
}
