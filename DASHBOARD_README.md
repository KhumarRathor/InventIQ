# Dashboard - Streamlit Web Application

Interactive real-time dashboard for inventory monitoring and procurement management.

## Technology Stack

- **Streamlit** - Dashboard framework
- **Plotly** - Interactive charts
- **Pandas** - Data manipulation
- **Requests** - HTTP client

## Features

### 📊 **Four Main Tabs**

**1. Inventory Overview**
- Complete product list with current stock levels
- Visual comparison charts (stock vs. threshold)
- Color-coded status indicators
- Total inventory value calculation

**2. Low Stock Alerts**
- Red warnings for critical items
- One-click AI analysis per product
- Real-time stock status updates

**3. AI Predictions**
- Interactive product selection
- 7-day sales history visualization
- Stockout prediction with projection charts
- Actionable recommendations

**4. Purchase Orders**
- All auto-generated purchase orders
- Filter by status (Suggested/Ordered/Received)
- **One-click approve/reject buttons**
- Cost calculations and delivery tracking

### 🎛️ **Control Panel**

- **Refresh Data** - Manual data refresh
- **Run AI Analysis** - Batch analysis of all low-stock items
- **System Health** - Backend + AI service status monitoring

### 📈 **Key Metrics Dashboard**

- Total Products count
- Low Stock Items count
- Pending Orders count
- Total Inventory Value

## Setup

### Prerequisites
- Python 3.11 or higher
- pip
- Backend and AI services running

### Installation

```bash
# Install dependencies
pip install -r requirements.txt

# Or install manually
pip install streamlit requests pandas plotly
```

### Configuration

Update `dashboard.py` if services are on different URLs:

```python
# Configuration
BACKEND_URL = "http://localhost:8080/api"
AI_SERVICE_URL = "http://localhost:8001"
```

### Run

```bash
# Start dashboard
streamlit run dashboard.py

# Or with custom port
streamlit run dashboard.py --server.port 8502
```

Dashboard opens at **http://localhost:8501**

## Project Structure

```
dashboard/
├── dashboard.py                 # Main application
├── requirements.txt            # Python dependencies
└── README.md
```

## Features Walkthrough

### 1. Viewing Inventory

Navigate to **📊 Inventory** tab:
- See all products in a sortable table
- View interactive bar chart (stock vs threshold)
- Check color-coded status (🟢 Good / 🟡 Low / 🔴 Critical)

### 2. Checking Alerts

Navigate to **🚨 Alerts** tab:
- See products below threshold
- Click **🤖 Analyze** to run AI prediction
- System auto-creates purchase order if critical

### 3. AI Predictions

Navigate to **🤖 AI Predictions** tab:
1. Select a product from dropdown
2. View sales history chart (last 7 days)
3. See AI prediction: "Stockout in X days"
4. Check projection graph showing when stock hits zero
5. Read recommendation (Critical/Warning/Good)

### 4. Managing Orders

Navigate to **📝 Purchase Orders** tab:
1. View all purchase orders
2. Filter by status
3. Click **✅ Approve** to accept order
4. Click **❌ Reject** to decline order
5. See order details (quantity, cost, delivery date)

### 5. Batch Analysis

In the sidebar:
1. Click **🤖 Run AI Analysis**
2. System analyzes ALL low-stock products
3. Auto-creates orders for critical items
4. Shows success message with count

## Dashboard Screenshots

### Inventory Overview
![Inventory](../screenshots/dashboard-inventory.png)

### AI Predictions
![Predictions](../screenshots/ai-predictions.png)

### Purchase Orders
![Orders](../screenshots/purchase-orders.png)

## Dependencies

```txt
streamlit==1.31.1
requests==2.31.0
pandas==2.2.0
plotly==5.18.0
```

## Caching Strategy

Dashboard uses Streamlit's caching for performance:

```python
@st.cache_data(ttl=30)
def fetch_products():
    # Cached for 30 seconds
    return requests.get(f"{BACKEND_URL}/products").json()
```

**Manual refresh:** Click "🔄 Refresh Data" in sidebar to clear cache.

## Error Handling

Dashboard gracefully handles errors:

**Backend Offline:**
```
Error fetching products: HTTPConnectionPool...
Backend API: 🔴 Offline
```

**AI Service Offline:**
```
AI Service: 🔴 Offline
Error getting AI prediction: Connection refused
```

**No Data:**
```
No products found in inventory
No low-stock products to analyze
```

## Customization

### Change Refresh Rate

Edit `dashboard.py`:
```python
@st.cache_data(ttl=60)  # Change to 60 seconds
```

### Change Theme

Create `.streamlit/config.toml`:
```toml
[theme]
primaryColor = "#FF4B4B"
backgroundColor = "#FFFFFF"
secondaryBackgroundColor = "#F0F2F6"
textColor = "#262730"
font = "sans serif"
```

### Add Custom Charts

Use Plotly for any visualization:
```python
import plotly.express as px

fig = px.bar(df, x='name', y='quantity', title='Stock Levels')
st.plotly_chart(fig, use_container_width=True)
```

## Keyboard Shortcuts

- `R` - Rerun the app
- `C` - Clear cache
- `?` - Show all keyboard shortcuts

## Deployment

### Local Deployment
Already covered above (streamlit run dashboard.py)

### Cloud Deployment (Streamlit Cloud)
1. Push code to GitHub
2. Go to https://streamlit.io/cloud
3. Connect GitHub repository
4. Select `dashboard/dashboard.py` as main file
5. Deploy!

**Note:** For full functionality, backend and AI services must also be deployed and URLs updated in config.

## Troubleshooting

### Dashboard won't start
```bash
# Update Streamlit
pip install --upgrade streamlit
```

### Connection errors
- Check if backend is running (http://localhost:8080/api/products)
- Check if AI service is running (http://localhost:8001/health)
- Look at sidebar system health indicators

### Charts not showing
```bash
# Install Plotly
pip install plotly
```

### Data not updating
- Click "🔄 Refresh Data" button in sidebar
- Check cache TTL settings (default 30 seconds)

### Port already in use
```bash
# Use different port
streamlit run dashboard.py --server.port 8502
```

## Performance

- **Load Time:** <2 seconds
- **Data Refresh:** 30 seconds cache
- **Chart Rendering:** Real-time
- **Memory Usage:** ~100-200MB

## Browser Compatibility

- ✅ Chrome (Recommended)
- ✅ Firefox
- ✅ Edge
- ✅ Safari
- ❌ IE11 (Not supported)

## Mobile Support

Dashboard is responsive and works on:
- 📱 Smartphones (portrait/landscape)
- 📱 Tablets
- 💻 Desktops
- 🖥️ Large monitors

## Future Enhancements

- [ ] User authentication
- [ ] Dark mode toggle
- [ ] Export to Excel/PDF
- [ ] Email report scheduling
- [ ] Mobile app version
- [ ] Real-time WebSocket updates
- [ ] Multi-language support
