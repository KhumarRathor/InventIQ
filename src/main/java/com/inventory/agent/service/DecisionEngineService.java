package com.inventory.agent.service;

import com.inventory.agent.dto.AIPredictionResponse;
import com.inventory.agent.model.Product;
import com.inventory.agent.model.PurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

@Service
public class DecisionEngineService {

    @Autowired
    private AIIntegrationService aiIntegrationService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    /**
     * DECISION ENGINE: Analyze product and automatically create purchase order if critical
     */
    public PurchaseOrder analyzeProcurement(Long productId) {
        // Get product details
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Generate sample sales history (in real app, this would come from sales database)
        List<Integer> salesHistory = generateSampleSalesHistory(product);

        // Call AI to predict stockout
        AIPredictionResponse prediction = aiIntegrationService.predictStockout(
                salesHistory, 
                product.getQuantity()
        );

        System.out.println("🧠 AI Prediction for " + product.getName() + ":");
        System.out.println("   Days until stockout: " + prediction.getPredictedStockoutDays());
        System.out.println("   Recommendation: " + prediction.getRecommendation());

        // PROCUREMENT DECISION LOGIC
        if (prediction.getPredictedStockoutDays() < 5) {
            // CRITICAL: Auto-create purchase order suggestion
            return createAutomaticPurchaseOrder(product, prediction);
        }

        return null; // No action needed
    }

    /**
     * Automatically create purchase order when stock is critical
     */
    private PurchaseOrder createAutomaticPurchaseOrder(Product product, AIPredictionResponse prediction) {
        // Calculate order quantity (2x threshold to ensure safety stock)
        Integer orderQuantity = (product.getThreshold() * 2) - product.getQuantity();
        
        if (orderQuantity <= 0) {
            orderQuantity = product.getThreshold();
        }

        // Create purchase order with "Suggested" status
        PurchaseOrder order = new PurchaseOrder();
        order.setProductId(product.getId());
        order.setQuantity(orderQuantity);
        order.setSupplier("Auto-Selected Supplier"); // In real app, use supplier selection logic
        order.setStatus("Suggested");
        order.setExpectedDelivery(LocalDate.now().plusDays(7)); // Assume 7-day delivery

        // Save to database
        PurchaseOrder savedOrder = purchaseOrderService.createPurchaseOrder(order);

        // Trigger notification (simplified - in real app, send email/Slack)
        sendNotification(product, prediction, savedOrder);

        System.out.println("✅ AUTO-CREATED Purchase Order #" + savedOrder.getId() + " for " + product.getName());

        return savedOrder;
    }

    /**
     * Notify procurement team
     */
    private void sendNotification(Product product, AIPredictionResponse prediction, PurchaseOrder order) {
        // In a real system, this would send email or Slack notification
        String notification = String.format(
            "🚨 PROCUREMENT ALERT\n" +
            "Product: %s\n" +
            "Current Stock: %d units\n" +
            "Predicted Stockout: %d days\n" +
            "Recommendation: %s\n" +
            "Action: Purchase Order #%d created (Status: Suggested)\n" +
            "Quantity to Order: %d units\n" +
            "Please review and approve.",
            product.getName(),
            product.getQuantity(),
            prediction.getPredictedStockoutDays(),
            prediction.getRecommendation(),
            order.getId(),
            order.getQuantity()
        );

        System.out.println("\n" + "=".repeat(60));
        System.out.println(notification);
        System.out.println("=".repeat(60) + "\n");

        // TODO: Integrate with email service (JavaMailSender) or Slack webhook
    }

    /**
     * Run procurement analysis for ALL low-stock products (batch job)
     */
    public List<PurchaseOrder> runBatchProcurementAnalysis() {
        List<PurchaseOrder> createdOrders = new ArrayList<>();

        // Get all low-stock products
        List<Product> lowStockProducts = productService.getLowStockProducts();

        System.out.println("🔍 Running batch procurement analysis for " + lowStockProducts.size() + " low-stock products...\n");

        for (Product product : lowStockProducts) {
            PurchaseOrder order = analyzeProcurement(product.getId());
            if (order != null) {
                createdOrders.add(order);
            }
        }

        System.out.println("✅ Batch analysis complete. Created " + createdOrders.size() + " purchase order suggestions.");

        return createdOrders;
    }

    /**
     * Generate sample sales history for demo
     * In real app, this would query actual sales data from database
     */
    private List<Integer> generateSampleSalesHistory(Product product) {
        // Simulate sales based on product name (for demo purposes)
        if (product.getName().contains("Widget B")) {
            return Arrays.asList(5, 6, 4, 5, 7, 6, 5); // High sales - causes critical alert
        } else if (product.getName().contains("Widget A")) {
            return Arrays.asList(2, 3, 2, 2, 3, 2, 2); // Medium sales
        } else {
            return Arrays.asList(3, 4, 3, 4, 3, 3, 4); // Normal sales
        }
    }
}
