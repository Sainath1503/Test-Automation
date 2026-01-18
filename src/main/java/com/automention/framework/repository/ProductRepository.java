package com.automention.framework.repository;

import com.automention.framework.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Product Repository Interface
 * Extends JpaRepository for database operations
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findAll();
    
    Product findByName(String name);
    
    List<Product> findByCategory(String category);
}
