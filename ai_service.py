from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import List
import statistics

app = FastAPI(title="Procurement AI Service")

# Enable CORS so Java backend can call this service
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Request/Response Models
class PredictionRequest(BaseModel):
    sales_history: List[int]  # Daily sales for past N days
    current_stock: int

class PredictionResponse(BaseModel):
    predicted_stockout_days: int
    average_daily_usage: float
    recommendation: str

@app.get("/")
def root():
    return {"message": "AI Prediction Service is running!", "version": "1.0"}

@app.post("/predict", response_model=PredictionResponse)
def predict_stockout(request: PredictionRequest):
    """
    Predict when a product will run out of stock.
    
    V1 Logic (Simple Math):
    1. Calculate average daily usage from sales history
    2. Divide current stock by average daily usage
    3. Return predicted days until stockout
    
    Future: Use ML (ARIMA, Prophet, LSTM) for better predictions
    """
    
    # Edge case: No sales history
    if not request.sales_history or len(request.sales_history) == 0:
        return PredictionResponse(
            predicted_stockout_days=999,
            average_daily_usage=0,
            recommendation="No sales history available. Cannot predict stockout."
        )
    
    # Calculate average daily usage
    average_daily_usage = statistics.mean(request.sales_history)
    
    # Edge case: No sales (average is 0)
    if average_daily_usage == 0:
        return PredictionResponse(
            predicted_stockout_days=999,
            average_daily_usage=0,
            recommendation="No sales detected. Stock level stable."
        )
    
    # Calculate days until stockout
    predicted_days = int(request.current_stock / average_daily_usage)
    
    # Generate recommendation
    if predicted_days <= 5:
        recommendation = "🚨 CRITICAL: Order immediately! Stockout in {} days.".format(predicted_days)
    elif predicted_days <= 10:
        recommendation = "⚠️  WARNING: Consider ordering soon. Stockout in {} days.".format(predicted_days)
    elif predicted_days <= 20:
        recommendation = "📊 MONITOR: Stock level acceptable for {} days.".format(predicted_days)
    else:
        recommendation = "✅ GOOD: Stock level healthy for {} days.".format(predicted_days)
    
    return PredictionResponse(
        predicted_stockout_days=predicted_days,
        average_daily_usage=round(average_daily_usage, 2),
        recommendation=recommendation
    )

@app.get("/health")
def health_check():
    return {"status": "healthy", "service": "AI Prediction"}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8001)
