# InventIQ 🎯
### AI-Powered Autonomous Procurement System

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-green.svg)](https://spring.io/projects/spring-boot)
[![Python](https://img.shields.io/badge/Python-3.11+-blue.svg)](https://www.python.org/)
[![FastAPI](https://img.shields.io/badge/FastAPI-0.115+-teal.svg)](https://fastapi.tiangolo.com/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![Streamlit](https://img.shields.io/badge/Streamlit-1.31+-red.svg)](https://streamlit.io/)

> An intelligent system that monitors inventory in real-time, predicts stockouts using AI, and automatically creates purchase orders before you run out of stock.

[🌐 Live Dashboard Demo](https://inventiq.streamlit.app) | [📖 Documentation](#) | [🎥 Demo Video](#)

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Installation](#installation)
- [Usage](#usage)
- [API Documentation](#api-documentation)
- [Future Enhancements](#future-enhancements)
- [Contact](#contact)

---

## 🎯 Overview

**InventIQ** is an autonomous procurement system that combines real-time inventory monitoring with AI-powered predictions to prevent stockouts and optimize supply chain operations. The system automatically analyzes sales patterns, predicts when products will run out, and generates purchase order suggestions for human approval.

### The Problem
- Manual inventory monitoring is time-consuming and error-prone
- Stockouts cause revenue loss and customer dissatisfaction  
- Traditional systems are reactive, not proactive
- No intelligent prediction of future demand

### The Solution
InventIQ uses **microservices architecture** with:
- **Java Spring Boot** backend for robust inventory management
- **Python AI service** for intelligent stockout predictions
- **Interactive dashboard** for real-time monitoring and control
- **Autonomous decision engine** that creates purchase orders automatically

---

## ✨ Features

### 🤖 **AI-Powered Predictions**
- Analyzes sales history to predict stockout dates
- Calculates average daily usage patterns
- Provides actionable recommendations (Critical/Warning/Good)

### 📊 **Real-Time Monitoring**
- Live inventory dashboard with interactive charts
- Low-stock alerts with color-coded warnings
- System health monitoring (Backend + AI service status)

### 🔄 **Autonomous Decision-Making**
- Automatically creates purchase orders when stock is critical (<5 days remaining)
- Human-in-the-loop approval workflow
- One-click approve/reject for suggested orders

### 📈 **Interactive Visualizations**
- Stock level vs. threshold comparison charts
- AI prediction graphs showing stockout projections
- Sales history trend analysis
- Purchase order management interface

---

## 🏗️ Architecture

```
┌─────────────────┐
│   Dashboard     │ ← Streamlit (Port 8501)
│  (Streamlit)    │
└────────┬────────┘
         │ HTTP Requests
         ↓
┌─────────────────┐
│  Java Backend   │ ← Spring Boot (Port 8080)
│  (Spring Boot)  │
└────────┬────────┘
         │
    ┌────┴────┐
    ↓         ↓
┌────────┐  ┌──────────────┐
│ MySQL  │  │  AI Service  │ ← FastAPI (Port 8001)
│   DB   │  │  (Python)    │
└────────┘  └──────────────┘
```

**Microservices Architecture:**
- **MySQL Database** - Stores inventory and purchase orders
- **Java Spring Boot Backend** - RESTful APIs & decision engine
- **Python AI Service** - Predictive analytics
- **Streamlit Dashboard** - Real-time visualization & control

---

## 🛠️ Tech Stack

**Backend:** Java 17, Spring Boot 3.2.1, Spring Data JPA, Hibernate, Maven  
**AI/ML:** Python 3.11+, FastAPI, Uvicorn, Pydantic  
**Database:** MySQL 8.0+, JDBC  
**Frontend:** Streamlit, Plotly, Pandas  
**Future:** Docker, JWT Authentication, Advanced ML (ARIMA, LSTM)

---

## 🚀 Installation

### Prerequisites
- Java JDK 17+
- Python 3.11+
- MySQL 8.0+
- Maven 3.6+

### Quick Start

```bash
# Clone repository
git clone https://github.com/KhumarRathor/InventIQ.git
cd InventIQ

# Setup database
mysql -u root -p < database/schema.sql

# Start backend (Terminal 1)
cd backend
mvn spring-boot:run

# Start AI service (Terminal 2)
cd ai-service
pip install -r requirements.txt
python ai_service.py

# Start dashboard (Terminal 3)
cd dashboard
pip install -r requirements.txt
streamlit run dashboard.py
```

Access dashboard at **http://localhost:8501**

---

## 💻 Usage

### Dashboard Features

**📊 Inventory Tab** - View all products with stock levels  
**🚨 Alerts Tab** - See critical low-stock warnings  
**🤖 AI Predictions** - Analyze products and view stockout forecasts  
**📝 Purchase Orders** - Approve/reject AI-generated orders  

### API Endpoints

```bash
# Get all products
GET http://localhost:8080/api/products

# Get low stock alerts
GET http://localhost:8080/api/alerts/low-stock

# Analyze product (triggers AI + auto-creates order if critical)
POST http://localhost:8080/api/procurement/analyze/2

# AI prediction
POST http://localhost:8001/predict
{
  "sales_history": [5,6,4,5,7],
  "current_stock": 15
}
```

---

## 📚 API Documentation

### Backend REST API

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/products` | GET | Get all products |
| `/api/products/{id}` | GET | Get product by ID |
| `/api/alerts/low-stock` | GET | Get low stock alerts |
| `/api/orders` | GET | Get all purchase orders |
| `/api/procurement/analyze/{id}` | POST | Analyze & auto-create order |
| `/api/procurement/batch-analyze` | POST | Analyze all low-stock products |
| `/api/procurement/ai-health` | GET | Check AI service status |

### AI Service API

**POST `/predict`** - Get stockout prediction

**Request:**
```json
{
  "sales_history": [5, 6, 4, 5, 7, 6, 5],
  "current_stock": 15
}
```

**Response:**
```json
{
  "predicted_stockout_days": 2,
  "average_daily_usage": 5.43,
  "recommendation": "🚨 CRITICAL: Order immediately! Stockout in 2 days."
}
```

---

## 🎯 Decision Engine Logic

```java
IF product.quantity < product.threshold THEN
    prediction = AI_Service.predict(salesHistory, currentStock)
    
    IF prediction.stockoutDays < 5 THEN
        orderQuantity = (threshold × 2) - currentStock
        CREATE PurchaseOrder(status = "Suggested")
        NOTIFY procurement_team
    END IF
END IF
```

---

## 🔮 Future Enhancements

- [ ] **Authentication** - Spring Security + JWT
- [ ] **Advanced ML** - ARIMA, Prophet, LSTM models
- [ ] **Email Notifications** - JavaMailSender integration
- [ ] **Testing** - JUnit + pytest (>80% coverage)
- [ ] **Docker** - Containerization & easy deployment
- [ ] **Supplier Selection** - Cost optimization algorithms
- [ ] **Mobile App** - React Native for on-the-go management

---

## 📧 Contact

**Khumar Rathor**  
B.Tech Computer Science & Engineering  
LNCT University, Bhopal

- 🐙 GitHub: [@KhumarRathor](https://github.com/KhumarRathor)
- 💼 LinkedIn: [Your LinkedIn](#)
- 📧 Email: your.email@example.com

---

## 🙏 Acknowledgments

**Academic Project** - LNCT University, Bhopal (Session: JAN-JUNE 2026)  
**Enrollment:** LNCDBTC21021  
**Guide:** Praveen Sharma

---

<div align="center">

**⭐ Star this repository if you find it helpful!**

**Made with ❤️ for B.Tech CSE Major Project**

![Project Rating](https://img.shields.io/badge/Rating-9.5%2F10-brightgreen)
![Status](https://img.shields.io/badge/Status-Active-success)

</div>
