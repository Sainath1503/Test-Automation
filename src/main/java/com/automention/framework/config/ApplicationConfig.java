package com.automention.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Application Configuration Class
 * Loads configuration properties from application.properties
 */
@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationConfig {

    @Value("${selenium.grid.url}")
    private String seleniumGridUrl;

    @Value("${selenium.browser}")
    private String browser;

    @Value("${selenium.headless}")
    private boolean headless;

    @Value("${selenium.timeout}")
    private int timeout;

    @Value("${web.url}")
    private String webUrl;

    @Value("${api.url}")
    private String apiUrl;

    @Value("${test.data.username}")
    private String username;

    @Value("${test.data.password}")
    private String password;

    @Value("${excel.config.path}")
    private String excelConfigPath;

    @Value("${elasticsearch.host}")
    private String elasticsearchHost;

    @Value("${elasticsearch.port}")
    private int elasticsearchPort;

    @Value("${elasticsearch.index}")
    private String elasticsearchIndex;

    @Value("${elasticsearch.enabled}")
    private boolean elasticsearchEnabled;

    @Value("${report.screenshot.path}")
    private String screenshotPath;

    @Value("${report.output.path}")
    private String reportOutputPath;

    @Value("${test.thread.count}")
    private int threadCount;

    // Getters
    public String getSeleniumGridUrl() {
        return seleniumGridUrl;
    }

    public String getBrowser() {
        return browser;
    }

    public boolean isHeadless() {
        return headless;
    }

    public int getTimeout() {
        return timeout;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getExcelConfigPath() {
        return excelConfigPath;
    }

    public String getElasticsearchHost() {
        return elasticsearchHost;
    }

    public int getElasticsearchPort() {
        return elasticsearchPort;
    }

    public String getElasticsearchIndex() {
        return elasticsearchIndex;
    }

    public boolean isElasticsearchEnabled() {
        return elasticsearchEnabled;
    }

    public String getScreenshotPath() {
        return screenshotPath;
    }

    public String getReportOutputPath() {
        return reportOutputPath;
    }

    public int getThreadCount() {
        return threadCount;
    }
}
