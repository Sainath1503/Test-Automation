package com.automention.framework.driver;

import com.automention.framework.config.ApplicationConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * WebDriver Manager for Selenium Grid
 * Handles WebDriver initialization and configuration
 */
@Component
public class WebDriverManager {

    private static final Logger logger = LogManager.getLogger(WebDriverManager.class);

    @Autowired
    private ApplicationConfig config;

    private ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /**
     * Initialize WebDriver using Selenium Grid
     */
    public WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        
        if (driver == null) {
            try {
                String gridUrl = config.getSeleniumGridUrl();
                String browser = config.getBrowser().toLowerCase();
                boolean headless = config.isHeadless();

                org.openqa.selenium.MutableCapabilities capabilities;

                switch (browser) {
                    case "chrome":
                        ChromeOptions chromeOptions = new ChromeOptions();
                        chromeOptions.addArguments("--start-maximized");
                        chromeOptions.addArguments("--disable-notifications");
                        chromeOptions.addArguments("--no-sandbox");
                        chromeOptions.addArguments("--disable-dev-shm-usage");
                        chromeOptions.addArguments("--disable-gpu");
                        chromeOptions.addArguments("--disable-software-rasterizer");
                        chromeOptions.addArguments("--disable-extensions");
                        chromeOptions.addArguments("--remote-allow-origins=*");
                        chromeOptions.setPageLoadStrategy(org.openqa.selenium.PageLoadStrategy.NORMAL);
                        if (headless) {
                            chromeOptions.addArguments("--headless=new");
                        }
                        capabilities = chromeOptions;
                        break;

                    case "firefox":
                        FirefoxOptions firefoxOptions = new FirefoxOptions();
                        firefoxOptions.addArguments("--width=1920");
                        firefoxOptions.addArguments("--height=1080");
                        if (headless) {
                            firefoxOptions.addArguments("--headless");
                        }
                        capabilities = firefoxOptions;
                        break;

                    case "edge":
                        EdgeOptions edgeOptions = new EdgeOptions();
                        edgeOptions.addArguments("--start-maximized");
                        edgeOptions.addArguments("--disable-dev-shm-usage");
                        if (headless) {
                            edgeOptions.addArguments("--headless=new");
                        }
                        capabilities = edgeOptions;
                        break;

                    default:
                        logger.warn("Unknown browser: {}. Defaulting to Chrome", browser);
                        ChromeOptions defaultOptions = new ChromeOptions();
                        defaultOptions.addArguments("--start-maximized");
                        defaultOptions.addArguments("--no-sandbox");
                        defaultOptions.addArguments("--disable-dev-shm-usage");
                        capabilities = defaultOptions;
                }

                driver = new RemoteWebDriver(new URL(gridUrl), capabilities);
                // Set timeouts - increase pageLoadTimeout for stability
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
                driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
                try {
                    driver.manage().window().maximize();
                } catch (Exception e) {
                    logger.warn("Could not maximize window: {}", e.getMessage());
                }

                driverThreadLocal.set(driver);
                logger.info("WebDriver initialized successfully for browser: {}", browser);

            } catch (MalformedURLException e) {
                logger.error("Error initializing WebDriver: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to initialize WebDriver", e);
            }
        }

        return driver;
    }

    /**
     * Quit and remove WebDriver instance
     */
    public void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver quit successfully");
            } catch (Exception e) {
                logger.error("Error quitting WebDriver: {}", e.getMessage(), e);
            } finally {
                driverThreadLocal.remove();
            }
        }
    }

    /**
     * Get current WebDriver instance
     */
    public WebDriver getCurrentDriver() {
        return driverThreadLocal.get();
    }
}
