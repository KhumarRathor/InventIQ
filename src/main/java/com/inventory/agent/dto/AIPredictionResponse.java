package com.inventory.agent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AIPredictionResponse {
    
    @JsonProperty("predicted_stockout_days")
    private Integer predictedStockoutDays;
    
    @JsonProperty("average_daily_usage")
    private Double averageDailyUsage;
    
    private String recommendation;

    // Constructors
    public AIPredictionResponse() {
    }

    public AIPredictionResponse(Integer predictedStockoutDays, Double averageDailyUsage, String recommendation) {
        this.predictedStockoutDays = predictedStockoutDays;
        this.averageDailyUsage = averageDailyUsage;
        this.recommendation = recommendation;
    }

    // Getters and Setters
    public Integer getPredictedStockoutDays() {
        return predictedStockoutDays;
    }

    public void setPredictedStockoutDays(Integer predictedStockoutDays) {
        this.predictedStockoutDays = predictedStockoutDays;
    }

    public Double getAverageDailyUsage() {
        return averageDailyUsage;
    }

    public void setAverageDailyUsage(Double averageDailyUsage) {
        this.averageDailyUsage = averageDailyUsage;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
}
