package com.inventory.agent.service;

import com.inventory.agent.dto.AIPredictionRequest;
import com.inventory.agent.dto.AIPredictionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AIIntegrationService {

    private final RestTemplate restTemplate;
    
    @Value("${ai.service.url:http://localhost:8001}")
    private String aiServiceUrl;

    public AIIntegrationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Call Python AI service to predict stockout
     */
    public AIPredictionResponse predictStockout(List<Integer> salesHistory, Integer currentStock) {
        try {
            AIPredictionRequest request = new AIPredictionRequest(salesHistory, currentStock);
            
            String url = aiServiceUrl + "/predict";
            
            AIPredictionResponse response = restTemplate.postForObject(
                url, 
                request, 
                AIPredictionResponse.class
            );
            
            return response;
            
        } catch (Exception e) {
            // If AI service is down, return default safe prediction
            System.err.println("Error calling AI service: " + e.getMessage());
            AIPredictionResponse fallback = new AIPredictionResponse();
            fallback.setPredictedStockoutDays(999);
            fallback.setAverageDailyUsage(0.0);
            fallback.setRecommendation("AI service unavailable. Manual review required.");
            return fallback;
        }
    }

    /**
     * Check if AI service is healthy
     */
    public boolean isAIServiceHealthy() {
        try {
            String url = aiServiceUrl + "/health";
            restTemplate.getForObject(url, String.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
