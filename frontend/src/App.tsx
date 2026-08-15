import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { ProtectedRoute } from './components/ProtectedRoute';

import { Login } from './pages/Login';
import { Register } from './pages/Register';
import { Dashboard } from './pages/Dashboard';
import { MyDocuments } from './pages/MyDocuments';
import { Opportunities } from './pages/Opportunities';
import { AIAssistant } from './pages/AIAssistant';
import { DigitalProfile } from './pages/DigitalProfile';
import { DocumentBundles } from './pages/DocumentBundles';
import { Notifications } from './pages/Notifications';
import { SecurityAudit } from './pages/SecurityAudit';
import { Settings } from './pages/Settings';

export const App: React.FC = () => {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          {/* Public Authentication Routes */}
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          {/* Protected Application Routes */}
          <Route element={<ProtectedRoute />}>
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/documents" element={<MyDocuments />} />
            <Route path="/opportunities" element={<Opportunities />} />
            <Route path="/assistant" element={<AIAssistant />} />
            <Route path="/profile" element={<DigitalProfile />} />
            <Route path="/bundles" element={<DocumentBundles />} />
            <Route path="/notifications" element={<Notifications />} />
            <Route path="/security" element={<SecurityAudit />} />
            <Route path="/settings" element={<Settings />} />
          </Route>

          {/* Fallback Redirect */}
          <Route path="*" element={<Navigate to="/dashboard" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
};

export default App;
