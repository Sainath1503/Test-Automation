package com.automention.framework.service;

import com.automention.framework.entity.Product;
import com.automention.framework.repository.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Database Service for Product operations
 * Handles database interactions using Hibernate/JPA
 */
@Service
public class DatabaseService {

    private static final Logger logger = LogManager.getLogger(DatabaseService.class);

    @Autowired
    private ProductRepository productRepository;

    /**
     * Get all products from database
     */
    public List<Product> getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            logger.info("Retrieved {} products from database", products.size());
            return products;
        } catch (Exception e) {
            logger.error("Error retrieving products from database: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Print all products to console
     */
    public void printAllProducts() {
        try {
            List<Product> products = getAllProducts();
            logger.info("=== Products from Database ===");
            System.out.println("\n=== Products from Database ===");
            for (Product product : products) {
                System.out.println(product.toString());
                logger.info("Product: {}", product.toString());
            }
            System.out.println("=== End of Products ===\n");
            logger.info("=== End of Products ===");
        } catch (Exception e) {
            logger.error("Error printing products: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Get product by name
     */
    public Product getProductByName(String name) {
        try {
            Product product = productRepository.findByName(name);
            logger.info("Product found by name '{}': {}", name, product);
            return product;
        } catch (Exception e) {
            logger.error("Error finding product by name: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Get products by category
     */
    public List<Product> getProductsByCategory(String category) {
        try {
            List<Product> products = productRepository.findByCategory(category);
            logger.info("Found {} products in category '{}'", products.size(), category);
            return products;
        } catch (Exception e) {
            logger.error("Error finding products by category: {}", e.getMessage(), e);
            throw e;
        }
    }
}
