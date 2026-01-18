package com.automention.framework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Excel Configuration Reader Utility
 * Reads test data and configuration from Excel files
 */
@Component
public class ExcelConfigReader {

    private static final Logger logger = LogManager.getLogger(ExcelConfigReader.class);

    @Value("${excel.config.path}")
    private String excelPath;

    /**
     * Read data from Excel file by sheet name and row number
     */
    public Map<String, String> readExcelData(String sheetName, int rowNum) {
        Map<String, String> dataMap = new HashMap<>();
        
        try (FileInputStream fis = new FileInputStream(excelPath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.error("Sheet '{}' not found in Excel file", sheetName);
                return dataMap;
            }

            Row headerRow = sheet.getRow(0);
            Row dataRow = sheet.getRow(rowNum);

            if (dataRow == null) {
                logger.error("Row {} not found in sheet '{}'", rowNum, sheetName);
                return dataMap;
            }

            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell headerCell = headerRow.getCell(i);
                Cell dataCell = dataRow.getCell(i);
                
                if (headerCell != null && dataCell != null) {
                    String key = getCellValueAsString(headerCell);
                    String value = getCellValueAsString(dataCell);
                    dataMap.put(key, value);
                }
            }

            logger.info("Successfully read data from Excel sheet '{}', row {}", sheetName, rowNum);
        } catch (IOException e) {
            logger.error("Error reading Excel file: {}", e.getMessage(), e);
        }

        return dataMap;
    }

    /**
     * Get cell value as String
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    /**
     * Get total row count in a sheet
     */
    public int getRowCount(String sheetName) {
        try (FileInputStream fis = new FileInputStream(excelPath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet != null) {
                return sheet.getLastRowNum() + 1;
            }
        } catch (IOException e) {
            logger.error("Error getting row count from Excel: {}", e.getMessage(), e);
        }
        return 0;
    }
}
