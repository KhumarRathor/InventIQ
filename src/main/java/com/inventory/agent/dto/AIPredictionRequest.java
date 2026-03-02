package com.inventory.agent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class AIPredictionRequest {

    @JsonProperty("sales_history")
    private List<Integer> salesHistory;

    @JsonProperty("current_stock")
    private Integer currentStock;

    // Constructors
    public AIPredictionRequest() {
    }

    public AIPredictionRequest(List<Integer> salesHistory, Integer currentStock) {
        this.salesHistory = salesHistory;
        this.currentStock = currentStock;
    }

    // Getters and Setters
    public List<Integer> getSalesHistory() {
        return salesHistory;
    }

    public void setSalesHistory(List<Integer> salesHistory) {
        this.salesHistory = salesHistory;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }
}
