package com.automention.framework.utils;

import com.automention.framework.driver.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Screenshot Utility
 * Captures screenshots during test execution
 */
@Component
public class ScreenshotUtil {

    private static final Logger logger = LogManager.getLogger(ScreenshotUtil.class);

    @Autowired
    private WebDriverManager webDriverManager;

    @Value("${report.screenshot.path}")
    private String screenshotPath;

    /**
     * Capture screenshot and save to file
     */
    public String captureScreenshot(String testName) {
        try {
            WebDriver driver = webDriverManager.getCurrentDriver();
            if (driver == null) {
                logger.warn("WebDriver is null, cannot capture screenshot");
                return null;
            }

            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);

            // Create directory if it doesn't exist
            File directory = new File(screenshotPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generate filename with timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = testName + "_" + timestamp + ".png";
            File destinationFile = new File(screenshotPath + fileName);

            FileUtils.copyFile(sourceFile, destinationFile);
            logger.info("Screenshot captured: {}", destinationFile.getAbsolutePath());

            return destinationFile.getAbsolutePath();
        } catch (IOException e) {
            logger.error("Error capturing screenshot: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Capture screenshot with custom filename
     */
    public String captureScreenshot(String testName, String customName) {
        try {
            WebDriver driver = webDriverManager.getCurrentDriver();
            if (driver == null) {
                logger.warn("WebDriver is null, cannot capture screenshot");
                return null;
            }

            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);

            File directory = new File(screenshotPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = testName + "_" + customName + ".png";
            File destinationFile = new File(screenshotPath + fileName);

            FileUtils.copyFile(sourceFile, destinationFile);
            logger.info("Screenshot captured: {}", destinationFile.getAbsolutePath());

            return destinationFile.getAbsolutePath();
        } catch (IOException e) {
            logger.error("Error capturing screenshot: {}", e.getMessage(), e);
            return null;
        }
    }
}
