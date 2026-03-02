package com.inventory.agent.service;

import com.inventory.agent.model.PurchaseOrder;
import com.inventory.agent.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    // Get all purchase orders
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    // Get purchase order by ID
    public Optional<PurchaseOrder> getPurchaseOrderById(Long id) {
        return purchaseOrderRepository.findById(id);
    }

    // Create new purchase order
    public PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder) {
        return purchaseOrderRepository.save(purchaseOrder);
    }

    // Update purchase order status
    public PurchaseOrder updateOrderStatus(Long id, String status) {
        PurchaseOrder order = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase order not found with id: " + id));
        
        order.setStatus(status);
        return purchaseOrderRepository.save(order);
    }

    // Get orders by status
    public List<PurchaseOrder> getOrdersByStatus(String status) {
        return purchaseOrderRepository.findByStatus(status);
    }

    // Get orders by product
    public List<PurchaseOrder> getOrdersByProduct(Long productId) {
        return purchaseOrderRepository.findByProductId(productId);
    }
}
