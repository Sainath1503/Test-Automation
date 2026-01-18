package com.automention.framework.pages;

import com.automention.framework.driver.WebDriverManager;
import io.cucumber.spring.ScenarioScope;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Login Page Object Model
 * Handles login page interactions
 */
@Component
@ScenarioScope
public class LoginPage {

    private static final Logger logger = LogManager.getLogger(LoginPage.class);

    @Autowired
    private WebDriverManager webDriverManager;

    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "submit")
    private WebElement submitButton;

    @FindBy(className = "post-title")
    private WebElement successMessage;

    @FindBy(linkText = "Log out")
    private WebElement logoutLink;

    private WebDriver driver;
    private WebDriverWait wait;

    /**
     * Initialize page elements
     */
    public void initElements() {
        driver = webDriverManager.getDriver();
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    /**
     * Enter username
     */
    public void enterUsername(String username) {
        try {
            if (wait == null || driver == null) {
                initElements();
            }
            wait.until(ExpectedConditions.visibilityOf(usernameField));
            usernameField.clear();
            usernameField.sendKeys(username);
            logger.info("Username entered: {}", username);
        } catch (Exception e) {
            logger.error("Error entering username: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Enter password
     */
    public void enterPassword(String password) {
        try {
            if (wait == null || driver == null) {
                initElements();
            }
            wait.until(ExpectedConditions.visibilityOf(passwordField));
            passwordField.clear();
            passwordField.sendKeys(password);
            logger.info("Password entered");
        } catch (Exception e) {
            logger.error("Error entering password: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Click submit button
     */
    public void clickSubmit() {
        try {
            if (wait == null || driver == null) {
                initElements();
            }
            wait.until(ExpectedConditions.elementToBeClickable(submitButton));
            submitButton.click();
            logger.info("Submit button clicked");
        } catch (Exception e) {
            logger.error("Error clicking submit button: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Perform login
     */
    public void login(String username, String password) {
        initElements();
        enterUsername(username);
        enterPassword(password);
        clickSubmit();
    }

    /**
     * Verify post-login success message
     */
    public boolean verifyLoginSuccess() {
        try {
            initElements();
            wait.until(ExpectedConditions.visibilityOf(successMessage));
            boolean isDisplayed = successMessage.isDisplayed();
            logger.info("Login success verification: {}", isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            logger.error("Error verifying login success: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Get success message text
     */
    public String getSuccessMessage() {
        try {
            initElements();
            wait.until(ExpectedConditions.visibilityOf(successMessage));
            return successMessage.getText();
        } catch (Exception e) {
            logger.error("Error getting success message: {}", e.getMessage(), e);
            return "";
        }
    }

    /**
     * Perform logout
     */
    public void logout() {
        try {
            initElements();
            wait.until(ExpectedConditions.elementToBeClickable(logoutLink));
            logoutLink.click();
            logger.info("Logout performed successfully");
        } catch (Exception e) {
            logger.error("Error performing logout: {}", e.getMessage(), e);
            throw e;
        }
    }
}
