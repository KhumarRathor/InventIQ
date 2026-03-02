package com.inventory.agent.repository;

import com.inventory.agent.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    
    // Find all purchase orders by product ID
    List<PurchaseOrder> findByProductId(Long productId);
    
    // Find purchase orders by status
    List<PurchaseOrder> findByStatus(String status);
}
