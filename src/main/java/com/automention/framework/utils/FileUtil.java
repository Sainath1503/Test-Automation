package com.automention.framework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * File Utility Class
 * Handles file operations
 */
public class FileUtil {

    private static final Logger logger = LogManager.getLogger(FileUtil.class);

    /**
     * Create directory if it doesn't exist
     */
    public static void createDirectory(String directoryPath) {
        try {
            Path path = Paths.get(directoryPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                logger.info("Directory created: {}", directoryPath);
            }
        } catch (Exception e) {
            logger.error("Error creating directory: {}", e.getMessage(), e);
        }
    }

    /**
     * Check if file exists
     */
    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * Get file extension
     */
    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex > 0 ? fileName.substring(lastDotIndex + 1) : "";
    }
}
