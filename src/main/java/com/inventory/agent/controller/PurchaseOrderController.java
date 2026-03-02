package com.inventory.agent.controller;

import com.inventory.agent.model.PurchaseOrder;
import com.inventory.agent.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    // GET /orders - Get all purchase orders
    @GetMapping("/orders")
    public ResponseEntity<List<PurchaseOrder>> getAllOrders() {
        List<PurchaseOrder> orders = purchaseOrderService.getAllPurchaseOrders();
        return ResponseEntity.ok(orders);
    }

    // GET /orders/{id} - Get single order
    @GetMapping("/orders/{id}")
    public ResponseEntity<PurchaseOrder> getOrderById(@PathVariable Long id) {
        return purchaseOrderService.getPurchaseOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /orders - Create new purchase order
    @PostMapping("/orders")
    public ResponseEntity<PurchaseOrder> createOrder(@RequestBody PurchaseOrder order) {
        PurchaseOrder newOrder = purchaseOrderService.createPurchaseOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }

    // PUT /orders/{id}/status - Update order status
    @PutMapping("/orders/{id}/status")
    public ResponseEntity<PurchaseOrder> updateOrderStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        try {
            PurchaseOrder updatedOrder = purchaseOrderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET /orders/status/{status} - Get orders by status
    @GetMapping("/orders/status/{status}")
    public ResponseEntity<List<PurchaseOrder>> getOrdersByStatus(@PathVariable String status) {
        List<PurchaseOrder> orders = purchaseOrderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    // GET /orders/product/{productId} - Get orders for a specific product
    @GetMapping("/orders/product/{productId}")
    public ResponseEntity<List<PurchaseOrder>> getOrdersByProduct(@PathVariable Long productId) {
        List<PurchaseOrder> orders = purchaseOrderService.getOrdersByProduct(productId);
        return ResponseEntity.ok(orders);
    }
}
