package com.inventory.agent.repository;

import com.inventory.agent.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Find products where quantity is less than threshold (low stock)
    @Query("SELECT p FROM Product p WHERE p.quantity < p.threshold")
    List<Product> findLowStockProducts();
}
