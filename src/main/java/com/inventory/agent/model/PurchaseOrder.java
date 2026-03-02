package com.inventory.agent.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Purchase_Order")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String supplier;

    @Column(nullable = false)
    private String status = "Pending";

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "expected_delivery")
    private LocalDate expectedDelivery;

    // Constructors
    public PurchaseOrder() {
    }

    public PurchaseOrder(Long productId, Integer quantity, String supplier) {
        this.productId = productId;
        this.quantity = quantity;
        this.supplier = supplier;
        this.status = "Pending";
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getExpectedDelivery() {
        return expectedDelivery;
    }

    public void setExpectedDelivery(LocalDate expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
    }

    @PrePersist
    protected void onCreate() {
        orderDate = LocalDateTime.now();
    }
}
