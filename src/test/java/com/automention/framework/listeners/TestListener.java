package com.automention.framework.listeners;

import com.automention.framework.utils.ElasticSearchUtil;
import com.automention.framework.utils.ScreenshotUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Test Listener for capturing test results
 * Integrates with Elasticsearch for reporting
 */
public class TestListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(TestListener.class);

    @Autowired
    private ElasticSearchUtil elasticSearchUtil;

    @Autowired
    private ScreenshotUtil screenshotUtil;

    @Override
    public void onTestSuccess(ITestResult result) {
        try {
            String testName = result.getMethod().getMethodName();
            String screenshotPath = screenshotUtil.captureScreenshot(testName);
            
            Map<String, Object> additionalData = new HashMap<>();
            additionalData.put("className", result.getTestClass().getName());
            additionalData.put("methodName", testName);
            
            elasticSearchUtil.sendTestResult(testName, "PASSED", screenshotPath, null, additionalData);
            logger.info("Test passed: {}", testName);
        } catch (Exception e) {
            logger.error("Error in test success listener: {}", e.getMessage(), e);
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            String testName = result.getMethod().getMethodName();
            String screenshotPath = screenshotUtil.captureScreenshot(testName + "_failure");
            String errorMessage = result.getThrowable().getMessage();
            
            Map<String, Object> additionalData = new HashMap<>();
            additionalData.put("className", result.getTestClass().getName());
            additionalData.put("methodName", testName);
            additionalData.put("stackTrace", getStackTrace(result.getThrowable()));
            
            elasticSearchUtil.sendTestResult(testName, "FAILED", screenshotPath, errorMessage, additionalData);
            logger.error("Test failed: {}", testName);
        } catch (Exception e) {
            logger.error("Error in test failure listener: {}", e.getMessage(), e);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        try {
            String testName = result.getMethod().getMethodName();
            elasticSearchUtil.sendTestResult(testName, "SKIPPED", null, "Test was skipped", null);
            logger.warn("Test skipped: {}", testName);
        } catch (Exception e) {
            logger.error("Error in test skipped listener: {}", e.getMessage(), e);
        }
    }

    private String getStackTrace(Throwable throwable) {
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}
