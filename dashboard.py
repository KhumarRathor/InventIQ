import streamlit as st
import requests
import pandas as pd
import plotly.express as px
import plotly.graph_objects as go
from datetime import datetime

# Configuration
BACKEND_URL = "http://localhost:8080/api"
AI_SERVICE_URL = "http://localhost:8001"

# Page config
st.set_page_config(
    page_title="Autonomous Procurement Dashboard",
    page_icon="📦",
    layout="wide"
)

# Custom CSS
st.markdown("""
<style>
    .stAlert {
        padding: 1rem;
        border-radius: 0.5rem;
    }
    .metric-card {
        background-color: #f0f2f6;
        padding: 1rem;
        border-radius: 0.5rem;
        margin: 0.5rem 0;
    }
</style>
""", unsafe_allow_html=True)

# Helper Functions
@st.cache_data(ttl=30)
def fetch_products():
    """Fetch all products from backend"""
    try:
        response = requests.get(f"{BACKEND_URL}/products")
        if response.status_code == 200:
            return response.json()
        return []
    except Exception as e:
        st.error(f"Error fetching products: {e}")
        return []

@st.cache_data(ttl=30)
def fetch_low_stock():
    """Fetch low stock alerts"""
    try:
        response = requests.get(f"{BACKEND_URL}/alerts/low-stock")
        if response.status_code == 200:
            return response.json()
        return []
    except Exception as e:
        st.error(f"Error fetching low stock alerts: {e}")
        return []

@st.cache_data(ttl=30)
def fetch_purchase_orders():
    """Fetch all purchase orders"""
    try:
        response = requests.get(f"{BACKEND_URL}/orders")
        if response.status_code == 200:
            return response.json()
        return []
    except Exception as e:
        st.error(f"Error fetching orders: {e}")
        return []

def get_ai_prediction(product_id):
    """Get AI prediction for a specific product"""
    try:
        response = requests.post(f"{BACKEND_URL}/procurement/analyze/{product_id}")
        if response.status_code == 200:
            return response.json()
        return None
    except Exception as e:
        st.error(f"Error getting AI prediction: {e}")
        return None

def update_order_status(order_id, new_status):
    """Update purchase order status"""
    try:
        response = requests.put(
            f"{BACKEND_URL}/orders/{order_id}/status",
            params={"status": new_status}
        )
        return response.status_code == 200
    except Exception as e:
        st.error(f"Error updating order: {e}")
        return False

def run_batch_analysis():
    """Run batch procurement analysis"""
    try:
        response = requests.post(f"{BACKEND_URL}/procurement/batch-analyze")
        if response.status_code == 200:
            return response.json()
        return None
    except Exception as e:
        st.error(f"Error running batch analysis: {e}")
        return None

# Header
st.title("📦 Autonomous Procurement Dashboard")
st.markdown("AI-Powered Inventory Management & Predictive Procurement")

# Sidebar
with st.sidebar:
    st.header("⚙️ Controls")
    
    if st.button("🔄 Refresh Data", use_container_width=True):
        st.cache_data.clear()
        st.rerun()
    
    if st.button("🤖 Run AI Analysis", use_container_width=True):
        with st.spinner("Running AI analysis on all low-stock products..."):
            result = run_batch_analysis()
            if result:
                st.success(f"✅ Created {result.get('orders_created', 0)} purchase order suggestions!")
                st.cache_data.clear()
    
    st.divider()
    
    # System Health
    st.subheader("🏥 System Health")
    
    # Check backend
    try:
        backend_response = requests.get(f"{BACKEND_URL}/products", timeout=2)
        backend_status = "🟢 Online" if backend_response.status_code == 200 else "🔴 Error"
    except:
        backend_status = "🔴 Offline"
    
    # Check AI service
    try:
        ai_response = requests.get(f"{AI_SERVICE_URL}/health", timeout=2)
        ai_status = "🟢 Online" if ai_response.status_code == 200 else "🔴 Error"
    except:
        ai_status = "🔴 Offline"
    
    st.write(f"**Backend API:** {backend_status}")
    st.write(f"**AI Service:** {ai_status}")

# Main Dashboard
products = fetch_products()
low_stock_products = fetch_low_stock()
orders = fetch_purchase_orders()

# Key Metrics
col1, col2, col3, col4 = st.columns(4)

with col1:
    st.metric(
        label="📦 Total Products",
        value=len(products)
    )

with col2:
    st.metric(
        label="⚠️ Low Stock Items",
        value=len(low_stock_products),
        delta=f"-{len(low_stock_products)} items" if len(low_stock_products) > 0 else "All Good"
    )

with col3:
    pending_orders = [o for o in orders if o['status'] == 'Suggested']
    st.metric(
        label="📋 Pending Orders",
        value=len(pending_orders)
    )

with col4:
    total_value = sum(p['price'] * p['quantity'] for p in products)
    st.metric(
        label="💰 Inventory Value",
        value=f"${total_value:,.2f}"
    )

st.divider()

# Tabs
tab1, tab2, tab3, tab4 = st.tabs(["📊 Inventory", "🚨 Alerts", "🤖 AI Predictions", "📝 Purchase Orders"])

# Tab 1: Inventory Overview
with tab1:
    st.header("Current Inventory")
    
    if products:
        # Create DataFrame
        df = pd.DataFrame(products)
        
        # Add stock status
        df['Stock Status'] = df.apply(
            lambda row: '🔴 Critical' if row['quantity'] < row['threshold'] 
            else '🟡 Low' if row['quantity'] < row['threshold'] * 1.5
            else '🟢 Good',
            axis=1
        )
        
        # Display table
        st.dataframe(
            df[['name', 'quantity', 'threshold', 'price', 'Stock Status']],
            use_container_width=True,
            hide_index=True
        )
        
        # Inventory Chart
        st.subheader("📈 Stock Levels vs. Thresholds")
        
        fig = go.Figure()
        
        fig.add_trace(go.Bar(
            name='Current Stock',
            x=df['name'],
            y=df['quantity'],
            marker_color='lightblue'
        ))
        
        fig.add_trace(go.Scatter(
            name='Minimum Threshold',
            x=df['name'],
            y=df['threshold'],
            mode='lines+markers',
            line=dict(color='red', width=2, dash='dash'),
            marker=dict(size=8)
        ))
        
        fig.update_layout(
            barmode='group',
            height=400,
            yaxis_title="Quantity",
            xaxis_title="Product"
        )
        
        st.plotly_chart(fig, use_container_width=True)
    else:
        st.info("No products found in inventory")

# Tab 2: Low Stock Alerts
with tab2:
    st.header("⚠️ Low Stock Alerts")
    
    if low_stock_products:
        for product in low_stock_products:
            with st.container():
                col1, col2, col3, col4 = st.columns([3, 2, 2, 2])
                
                with col1:
                    st.markdown(f"### 🔴 {product['name']}")
                
                with col2:
                    st.metric("Current Stock", product['quantity'])
                
                with col3:
                    st.metric("Threshold", product['threshold'])
                
                with col4:
                    if st.button(f"🤖 Analyze", key=f"analyze_{product['id']}"):
                        with st.spinner("Getting AI prediction..."):
                            result = get_ai_prediction(product['id'])
                            if result and 'order' in result:
                                st.success("✅ Purchase order created!")
                                st.cache_data.clear()
                            else:
                                st.info("No action needed at this time")
                
                st.divider()
    else:
        st.success("🎉 All products are adequately stocked!")

# Tab 3: AI Predictions
with tab3:
    st.header("🤖 AI Stockout Predictions")
    
    if low_stock_products:
        st.info("💡 Select a product to see AI predictions")
        
        product_names = {p['id']: p['name'] for p in low_stock_products}
        selected_product_id = st.selectbox(
            "Select Product",
            options=list(product_names.keys()),
            format_func=lambda x: product_names[x]
        )
        
        if selected_product_id:
            product = next(p for p in low_stock_products if p['id'] == selected_product_id)
            
            col1, col2 = st.columns([1, 1])
            
            with col1:
                st.subheader("Product Details")
                st.write(f"**Name:** {product['name']}")
                st.write(f"**Current Stock:** {product['quantity']} units")
                st.write(f"**Threshold:** {product['threshold']} units")
                st.write(f"**Price:** ${product['price']}")
                
                # Simulated sales history (in real app, this would come from database)
                st.subheader("📊 Sales History (Last 7 Days)")
                sales_data = [5, 6, 4, 5, 7, 6, 5] if 'Widget B' in product['name'] else [2, 3, 2, 2, 3, 2, 2]
                
                fig = px.line(
                    x=list(range(1, 8)),
                    y=sales_data,
                    labels={'x': 'Days Ago', 'y': 'Units Sold'},
                    title='Daily Sales Trend'
                )
                st.plotly_chart(fig, use_container_width=True)
            
            with col2:
                st.subheader("🔮 AI Prediction")
                
                # Get prediction from AI
                try:
                    ai_response = requests.post(
                        f"{AI_SERVICE_URL}/predict",
                        json={
                            "sales_history": sales_data,
                            "current_stock": product['quantity']
                        }
                    )
                    
                    if ai_response.status_code == 200:
                        prediction = ai_response.json()
                        
                        # Display prediction
                        days_left = prediction['predicted_stockout_days']
                        avg_usage = prediction['average_daily_usage']
                        recommendation = prediction['recommendation']
                        
                        # Color code based on urgency
                        if days_left <= 5:
                            color = "red"
                            icon = "🚨"
                        elif days_left <= 10:
                            color = "orange"
                            icon = "⚠️"
                        else:
                            color = "green"
                            icon = "✅"
                        
                        st.markdown(f"### {icon} Stockout in {days_left} days")
                        st.markdown(f"**Average Daily Usage:** {avg_usage:.2f} units/day")
                        st.markdown(f"**Recommendation:**")
                        st.markdown(f"_{recommendation}_")
                        
                        # Projection chart
                        projection_days = min(days_left + 5, 30)
                        days = list(range(projection_days + 1))
                        projected_stock = [max(0, product['quantity'] - (avg_usage * d)) for d in days]
                        
                        fig = go.Figure()
                        
                        fig.add_trace(go.Scatter(
                            x=days,
                            y=projected_stock,
                            mode='lines+markers',
                            name='Projected Stock',
                            line=dict(color='blue', width=3)
                        ))
                        
                        fig.add_hline(
                            y=product['threshold'],
                            line_dash="dash",
                            line_color="red",
                            annotation_text="Minimum Threshold"
                        )
                        
                        fig.add_vline(
                            x=days_left,
                            line_dash="dash",
                            line_color="orange",
                            annotation_text=f"Stockout (Day {days_left})"
                        )
                        
                        fig.update_layout(
                            title="Stock Projection",
                            xaxis_title="Days from Now",
                            yaxis_title="Stock Level",
                            height=400
                        )
                        
                        st.plotly_chart(fig, use_container_width=True)
                        
                    else:
                        st.error("Failed to get AI prediction")
                        
                except Exception as e:
                    st.error(f"Error: {e}")
    else:
        st.info("No low-stock products to analyze")

# Tab 4: Purchase Orders
with tab4:
    st.header("📝 Purchase Orders")
    
    if orders:
        # Filter by status
        status_filter = st.radio(
            "Filter by Status",
            options=["All", "Suggested", "Ordered", "Received"],
            horizontal=True
        )
        
        filtered_orders = orders if status_filter == "All" else [o for o in orders if o['status'] == status_filter]
        
        if filtered_orders:
            for order in filtered_orders:
                with st.container():
                    col1, col2, col3, col4, col5 = st.columns([2, 2, 2, 2, 2])
                    
                    # Get product name
                    product = next((p for p in products if p['id'] == order['productId']), None)
                    product_name = product['name'] if product else f"Product #{order['productId']}"
                    
                    with col1:
                        status_icon = "📋" if order['status'] == "Suggested" else "✅" if order['status'] == "Ordered" else "📦"
                        st.markdown(f"**{status_icon} Order #{order['id']}**")
                        st.write(product_name)
                    
                    with col2:
                        st.write(f"**Quantity:** {order['quantity']} units")
                        st.write(f"**Supplier:** {order['supplier']}")
                    
                    with col3:
                        st.write(f"**Status:** {order['status']}")
                        order_date = datetime.fromisoformat(order['orderDate'].replace('Z', '+00:00'))
                        st.write(f"**Date:** {order_date.strftime('%Y-%m-%d')}")
                    
                    with col4:
                        if order.get('expectedDelivery'):
                            st.write(f"**Delivery:** {order['expectedDelivery']}")
                        
                        if product:
                            total_cost = product['price'] * order['quantity']
                            st.write(f"**Cost:** ${total_cost:.2f}")
                    
                    with col5:
                        if order['status'] == 'Suggested':
                            col_a, col_b = st.columns(2)
                            with col_a:
                                if st.button("✅ Approve", key=f"approve_{order['id']}", use_container_width=True):
                                    if update_order_status(order['id'], "Ordered"):
                                        st.success("Approved!")
                                        st.cache_data.clear()
                                        st.rerun()
                            with col_b:
                                if st.button("❌ Reject", key=f"reject_{order['id']}", use_container_width=True):
                                    if update_order_status(order['id'], "Cancelled"):
                                        st.success("Rejected!")
                                        st.cache_data.clear()
                                        st.rerun()
                    
                    st.divider()
        else:
            st.info(f"No {status_filter.lower()} orders found")
    else:
        st.info("No purchase orders yet. Run AI analysis to generate suggestions!")

# Footer
st.divider()
st.markdown("""
<div style='text-align: center; color: gray;'>
    <p>🤖 Autonomous Procurement System v1.0 | Powered by AI</p>
</div>
""", unsafe_allow_html=True)
