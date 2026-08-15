import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { User, AuthResponse } from '../types';
import { authApi } from '../services/api';

interface AuthContextType {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (email: string, password: string) => Promise<void>;
  register: (fullName: string, email: string, password: string) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(() => {
    const savedUser = localStorage.getItem('docmind_user');
    return savedUser ? JSON.parse(savedUser) : null;
  });
  const [token, setToken] = useState<string | null>(() => {
    return localStorage.getItem('docmind_token');
  });
  const [isLoading, setIsLoading] = useState<boolean>(true);

  useEffect(() => {
    const initAuth = async () => {
      if (token) {
        try {
          const response = await authApi.getCurrentUser();
          if (response.success) {
            setUser(response.data);
            localStorage.setItem('docmind_user', JSON.stringify(response.data));
          } else {
            logout();
          }
        } catch {
          logout();
        }
      }
      setIsLoading(false);
    };

    initAuth();
  }, [token]);

  const login = async (email: string, password: string) => {
    const response = await authApi.login(email, password);
    if (response.success && response.data) {
      setToken(response.data.token);
      setUser(response.data.user);
      localStorage.setItem('docmind_token', response.data.token);
      localStorage.setItem('docmind_user', JSON.stringify(response.data.user));
    } else {
      throw new Error(response.message || 'Login failed');
    }
  };

  const register = async (fullName: string, email: string, password: string) => {
    const response = await authApi.register(fullName, email, password);
    if (response.success && response.data) {
      setToken(response.data.token);
      setUser(response.data.user);
      localStorage.setItem('docmind_token', response.data.token);
      localStorage.setItem('docmind_user', JSON.stringify(response.data.user));
    } else {
      throw new Error(response.message || 'Registration failed');
    }
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    localStorage.removeItem('docmind_token');
    localStorage.removeItem('docmind_user');
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        isAuthenticated: !!user && !!token,
        isLoading,
        login,
        register,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
