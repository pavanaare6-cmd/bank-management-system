# Frontend Setup - NexBank

## Quick Start

### Option 1: Open directly in browser
1. Navigate to: `frontend/index.html`
2. Double-click or open with your browser
3. Frontend will load at this file location

### Option 2: Use Live Server (Recommended for Development)

#### VS Code:
1. Install "Live Server" extension by Ritwick Dey
2. Right-click on `index.html`
3. Select "Open with Live Server"
4. Server runs at: `http://localhost:5500`

#### Python:
```bash
# Python 3.x
cd frontend
python -m http.server 8000

# Python 2.x
python -m SimpleHTTPServer 8000
```
Then open: `http://localhost:8000`

#### Node.js (http-server):
```bash
npm install -g http-server
cd frontend
http-server
```

## Configuration

The frontend communicates with backend API at:
```javascript
const API_BASE = 'http://localhost:8080/api';
```

If you change the backend port, update `js/utils.js`:
```javascript
const API_BASE = 'http://localhost:8081/api';  // If backend runs on port 8081
```

## Pages

- **index.html** - Login page
- **pages/register.html** - User registration
- **pages/dashboard.html** - Main dashboard (requires auth)
- **pages/deposit.html** - Deposit money
- **pages/withdraw.html** - Withdraw money
- **pages/transfer.html** - Transfer between accounts
- **pages/balance.html** - View account history

## Features

✅ Responsive design (works on mobile/tablet/desktop)
✅ JWT authentication
✅ Real-time account balance
✅ Transaction history
✅ Account management
✅ Deposit/Withdraw/Transfer operations

## Storage

User data is stored in **localStorage**:
- `bankToken` - JWT authentication token
- `bankUser` - User info (name, email, ID)

## Styling

- **CSS**: `css/style.css` - Contains all styles
- Design System: CSS Variables for theming
- Colors: Professional banking UI
- Responsive: Mobile-first approach

## Scripts

**Utils**: `js/utils.js`
- Token management
- API requests
- UI helpers (alerts, loading states)
- Currency/date formatting
- Auth guards

## Testing

1. Open Developer Console (F12)
2. Check Console tab for any JavaScript errors
3. Check Network tab to see API calls
4. Verify backend is running at http://localhost:8080

## Troubleshooting

**"Cannot GET /api/...*"**
- Ensure backend is running
- Check backend port (default 8080)
- Verify API_BASE in utils.js is correct

**Page shows blank**
- Open Developer Console (F12)
- Check for JavaScript errors
- Check Network tab for failed requests

**CSS not loading**
- Verify `css/style.css` path is correct
- Clear browser cache (Ctrl+Shift+Del)
- Hard refresh (Ctrl+Shift+R)

