package com.automention.framework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Logger Utility Class
 * Provides centralized logging functionality
 */
public class LoggerUtil {

    /**
     * Get logger instance for a class
     */
    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }

    /**
     * Get logger instance by name
     */
    public static Logger getLogger(String name) {
        return LogManager.getLogger(name);
    }
}
