"""
Test script for AI Prediction Service
Run this after starting the AI service to test it
"""

import requests
import json

# Test data
test_cases = [
    {
        "name": "Widget B - Critical Stock",
        "data": {
            "sales_history": [5, 6, 4, 5, 7, 6, 5],  # Selling ~5 per day
            "current_stock": 15  # Only 15 left = ~3 days
        }
    },
    {
        "name": "Widget A - Healthy Stock",
        "data": {
            "sales_history": [2, 3, 2, 2, 3, 2, 2],  # Selling ~2 per day
            "current_stock": 50  # 50 in stock = ~25 days
        }
    },
    {
        "name": "Widget C - Very Healthy",
        "data": {
            "sales_history": [3, 4, 3, 4, 3, 3, 4],  # Selling ~3.4 per day
            "current_stock": 100  # 100 in stock = ~29 days
        }
    }
]

print("🧪 Testing AI Prediction Service\n")
print("=" * 60)

for test in test_cases:
    print(f"\n📦 Testing: {test['name']}")
    print(f"Sales History: {test['data']['sales_history']}")
    print(f"Current Stock: {test['data']['current_stock']}")
    
    try:
        response = requests.post(
            "http://localhost:8001/predict",
            json=test['data']
        )
        
        if response.status_code == 200:
            result = response.json()
            print(f"\n✅ Prediction Results:")
            print(f"   Predicted Stockout: {result['predicted_stockout_days']} days")
            print(f"   Average Daily Usage: {result['average_daily_usage']} units/day")
            print(f"   Recommendation: {result['recommendation']}")
        else:
            print(f"❌ Error: {response.status_code}")
            
    except Exception as e:
        print(f"❌ Failed to connect. Is the AI service running on port 8001?")
        print(f"   Error: {e}")
        break

print("\n" + "=" * 60)
print("✅ Testing Complete!")
