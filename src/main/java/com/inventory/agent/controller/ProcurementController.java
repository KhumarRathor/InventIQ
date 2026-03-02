package com.inventory.agent.controller;

import com.inventory.agent.dto.AIPredictionResponse;
import com.inventory.agent.model.PurchaseOrder;
import com.inventory.agent.service.AIIntegrationService;
import com.inventory.agent.service.DecisionEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/procurement")
@CrossOrigin(origins = "*")
public class ProcurementController {

    @Autowired
    private DecisionEngineService decisionEngineService;

    @Autowired
    private AIIntegrationService aiIntegrationService;

    /**
     * Analyze single product and auto-create purchase order if critical
     */
    @PostMapping("/analyze/{productId}")
    public ResponseEntity<?> analyzeProduct(@PathVariable Long productId) {
        try {
            PurchaseOrder order = decisionEngineService.analyzeProcurement(productId);
            
            if (order != null) {
                return ResponseEntity.ok(Map.of(
                    "message", "Purchase order created automatically",
                    "order", order,
                    "action", "Suggested purchase order created due to critical stock level"
                ));
            } else {
                return ResponseEntity.ok(Map.of(
                    "message", "No action needed",
                    "action", "Stock level is acceptable"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage()
            ));
        }
    }

    /**
     * Run batch analysis on ALL low-stock products
     * This would typically be scheduled as a nightly job
     */
    @PostMapping("/batch-analyze")
    public ResponseEntity<?> runBatchAnalysis() {
        List<PurchaseOrder> orders = decisionEngineService.runBatchProcurementAnalysis();
        
        return ResponseEntity.ok(Map.of(
            "message", "Batch procurement analysis complete",
            "orders_created", orders.size(),
            "orders", orders
        ));
    }

    /**
     * Test AI prediction directly (for debugging)
     */
    @PostMapping("/predict-test")
    public ResponseEntity<AIPredictionResponse> testPrediction(@RequestBody Map<String, Object> request) {
        @SuppressWarnings("unchecked")
        List<Integer> salesHistory = (List<Integer>) request.get("sales_history");
        Integer currentStock = (Integer) request.get("current_stock");
        
        AIPredictionResponse prediction = aiIntegrationService.predictStockout(salesHistory, currentStock);
        return ResponseEntity.ok(prediction);
    }

    /**
     * Check AI service health
     */
    @GetMapping("/ai-health")
    public ResponseEntity<?> checkAIHealth() {
        boolean healthy = aiIntegrationService.isAIServiceHealthy();
        
        return ResponseEntity.ok(Map.of(
            "ai_service_status", healthy ? "healthy" : "unavailable",
            "message", healthy ? "AI service is responding" : "AI service is not accessible"
        ));
    }
}
