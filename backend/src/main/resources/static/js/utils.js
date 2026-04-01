// ============================================================
// Bank Management System - Shared Utilities
// All pages include this file for common functionality
// ============================================================

const API_BASE = '/api';

// ---- Token Management ----
// Store and retrieve JWT token from localStorage

function getToken() {
    return localStorage.getItem('bankToken');
}

function setToken(token) {
    localStorage.setItem('bankToken', token);
}

function removeToken() {
    localStorage.removeItem('bankToken');
}

function getUser() {
    const userData = localStorage.getItem('bankUser');
    return userData ? JSON.parse(userData) : null;
}

function setUser(user) {
    localStorage.setItem('bankUser', JSON.stringify(user));
}

function removeUser() {
    localStorage.removeItem('bankUser');
}

// ---- Auth Guards ----

/**
 * Call this on protected pages (dashboard, deposit, etc.)
 * Redirects to login if no token found.
 */
function requireAuth() {
    if (!getToken()) {
        window.location.href = '../index.html';
        return false;
    }
    return true;
}

/**
 * Call this on auth pages (login, register).
 * Redirects to dashboard if already logged in.
 */
function redirectIfLoggedIn() {
    if (getToken()) {
        window.location.href = 'pages/dashboard.html';
    }
}

// ---- Logout ----
function logout() {
    removeToken();
    removeUser();
    window.location.href = '../index.html';
}

// ---- API Request Helper ----
/**
 * Makes an authenticated API request.
 * Automatically adds Authorization header with JWT token.
 *
 * @param {string} endpoint - API path, e.g. "/accounts"
 * @param {string} method - HTTP method: GET, POST, PUT, DELETE
 * @param {object} body - Request body (for POST/PUT)
 * @param {number} timeout - Request timeout in milliseconds (default: 10000)
 * @returns {Promise<object>} - { ok, status, data, error }
 */
async function apiRequest(endpoint, method = 'GET', body = null, timeout = 10000) {
    const headers = {
        'Content-Type': 'application/json',
    };

    // Add JWT token if available
    const token = getToken();
    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    const options = { method, headers };
    if (body) {
        options.body = JSON.stringify(body);
    }

    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), timeout);
    options.signal = controller.signal;

    try {
        const response = await fetch(`${API_BASE}${endpoint}`, options);
        clearTimeout(timeoutId);
        
        let data;
        try {
            data = await response.json();
        } catch (e) {
            data = { success: false, message: 'Invalid server response' };
        }

        // If 401 Unauthorized, token expired - redirect to login
        if (response.status === 401) {
            removeToken();
            removeUser();
            window.location.href = '../index.html';
            return { ok: false, status: 401, data, error: 'Session expired. Please login again.' };
        }

        return { ok: response.ok, status: response.status, data, error: null };
    } catch (err) {
        clearTimeout(timeoutId);
        if (err.name === 'AbortError') {
            return { ok: false, status: 408, error: 'Request timeout. Is the backend running?' };
        }
        return { ok: false, status: 0, error: 'Network error. Cannot connect to server.' };
    }
}

// ---- UI Helpers ----

/**
 * Show an alert message on the page.
 * @param {string} elementId - ID of the alert div
 * @param {string} message - Message to display
 * @param {string} type - 'success', 'error', or 'info'
 */
function showAlert(elementId, message, type = 'info') {
    const alertEl = document.getElementById(elementId);
    if (!alertEl) return;

    alertEl.className = `alert alert-${type} show`;
    alertEl.textContent = message;

    // Auto-hide success messages after 5 seconds
    if (type === 'success') {
        setTimeout(() => hideAlert(elementId), 5000);
    }
}

function hideAlert(elementId) {
    const alertEl = document.getElementById(elementId);
    if (alertEl) alertEl.className = 'alert';
}

/**
 * Set button loading state (show spinner, disable button).
 */
function setButtonLoading(buttonId, isLoading, originalText = 'Submit') {
    const btn = document.getElementById(buttonId);
    if (!btn) return;

    if (isLoading) {
        btn.disabled = true;
        btn.innerHTML = '<span class="spinner"></span> Processing...';
    } else {
        btn.disabled = false;
        btn.textContent = originalText;
    }
}

/**
 * Format a number as currency (Indian Rupees).
 */
function formatCurrency(amount) {
    return '₹' + parseFloat(amount).toLocaleString('en-IN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });
}

/**
 * Format a date string nicely.
 */
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-IN', {
        day: '2-digit',
        month: 'short',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

/**
 * Get the first letter(s) of a name for avatar display.
 */
function getInitials(name) {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
}

/**
 * Populate the navbar with user info.
 * Call this on every protected page.
 */
function populateNavbar() {
    const user = getUser();
    if (!user) return;

    const avatarEl = document.getElementById('userAvatar');
    const nameEl = document.getElementById('userName');

    if (avatarEl) avatarEl.textContent = getInitials(user.name);
    if (nameEl) nameEl.textContent = user.name;
}

/**
 * Load and populate the account selector dropdown.
 * Used on deposit, withdraw, transfer pages.
 */
async function loadAccountSelector(selectId) {
    const select = document.getElementById(selectId);
    if (!select) return;

    select.innerHTML = '<option value="">Loading accounts...</option>';

    const result = await apiRequest('/accounts');
    if (!result || !result.ok) {
        select.innerHTML = '<option value="">Failed to load accounts</option>';
        return;
    }

    const accounts = result.data.data;
    if (!accounts || accounts.length === 0) {
        select.innerHTML = '<option value="">No accounts found - create one first</option>';
        return;
    }

    select.innerHTML = '<option value="">-- Select Account --</option>';
    accounts.forEach(acc => {
        const option = document.createElement('option');
        option.value = acc.accountId;
        option.textContent = `${acc.accountNumber} (${acc.accountType}) — ${formatCurrency(acc.balance)}`;
        select.appendChild(option);
    });
}

/**
 * Returns the CSS badge class and label for a transaction type.
 */
function getTransactionBadge(type) {
    const map = {
        'DEPOSIT':      { cls: 'badge-deposit',      icon: '↓', label: 'Deposit' },
        'WITHDRAWAL':   { cls: 'badge-withdrawal',   icon: '↑', label: 'Withdrawal' },
        'TRANSFER_OUT': { cls: 'badge-transfer-out', icon: '→', label: 'Transfer Out' },
        'TRANSFER_IN':  { cls: 'badge-transfer-in',  icon: '←', label: 'Transfer In' },
    };
    return map[type] || { cls: '', icon: '•', label: type };
}
