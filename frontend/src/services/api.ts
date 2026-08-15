import axios from 'axios';
import { ApiResponse, AuthResponse, User, DocumentItem, UserProfile, OpportunityItem, ReminderItem, DocumentBundleItem, AuditLogItem } from '../types';

const API_BASE_URL = '/api';

export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('docmind_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('docmind_token');
      localStorage.removeItem('docmind_user');
      if (window.location.pathname !== '/login' && window.location.pathname !== '/register') {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export const authApi = {
  login: async (email: string, password: string): Promise<ApiResponse<AuthResponse>> => {
    const response = await api.post<ApiResponse<AuthResponse>>('/auth/login', { email, password });
    return response.data;
  },
  register: async (fullName: string, email: string, password: string): Promise<ApiResponse<AuthResponse>> => {
    const response = await api.post<ApiResponse<AuthResponse>>('/auth/register', { fullName, email, password });
    return response.data;
  },
  getCurrentUser: async (): Promise<ApiResponse<User>> => {
    const response = await api.get<ApiResponse<User>>('/auth/me');
    return response.data;
  },
};

export const documentsApi = {
  getDocuments: async (category?: string, status?: string): Promise<ApiResponse<DocumentItem[]>> => {
    const params = new URLSearchParams();
    if (category) params.append('category', category);
    if (status) params.append('status', status);
    const response = await api.get<ApiResponse<DocumentItem[]>>(`/documents?${params.toString()}`);
    return response.data;
  },

  uploadDocument: async (
    file: File,
    category?: string,
    documentType?: string,
    issueDate?: string,
    expiryDate?: string
  ): Promise<ApiResponse<DocumentItem>> => {
    const formData = new FormData();
    formData.append('file', file);
    if (category) formData.append('category', category);
    if (documentType) formData.append('documentType', documentType);
    if (issueDate) formData.append('issueDate', issueDate);
    if (expiryDate) formData.append('expiryDate', expiryDate);

    const response = await api.post<ApiResponse<DocumentItem>>('/documents/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  },

  getDocumentDetails: async (id: number): Promise<ApiResponse<DocumentItem>> => {
    const response = await api.get<ApiResponse<DocumentItem>>(`/documents/${id}`);
    return response.data;
  },

  deleteDocument: async (id: number): Promise<ApiResponse<null>> => {
    const response = await api.delete<ApiResponse<null>>(`/documents/${id}`);
    return response.data;
  },

  getDownloadUrl: (id: number): string => {
    const token = localStorage.getItem('docmind_token');
    return `/api/documents/${id}/download?token=${token}`;
  },
};

export const profileApi = {
  getProfile: async (): Promise<ApiResponse<UserProfile>> => {
    const response = await api.get<ApiResponse<UserProfile>>('/profile');
    return response.data;
  },

  updateProfile: async (data: Partial<UserProfile>): Promise<ApiResponse<UserProfile>> => {
    const response = await api.put<ApiResponse<UserProfile>>('/profile', data);
    return response.data;
  },

  syncProfileFromDocuments: async (): Promise<ApiResponse<UserProfile>> => {
    const response = await api.post<ApiResponse<UserProfile>>('/profile/sync');
    return response.data;
  },
};

export const opportunitiesApi = {
  getOpportunities: async (query?: string, type?: string, location?: string, page?: number): Promise<ApiResponse<OpportunityItem[]>> => {
    const params = new URLSearchParams();
    if (query) params.append('query', query);
    if (type) params.append('type', type);
    if (location) params.append('location', location);
    if (page) params.append('page', page.toString());
    const response = await api.get<ApiResponse<OpportunityItem[]>>(`/opportunities?${params.toString()}`);
    return response.data;
  },

  getTopMatches: async (): Promise<ApiResponse<OpportunityItem[]>> => {
    const response = await api.get<ApiResponse<OpportunityItem[]>>('/opportunities/top-matches');
    return response.data;
  },
};

export const remindersApi = {
  getReminders: async (): Promise<ApiResponse<ReminderItem[]>> => {
    const response = await api.get<ApiResponse<ReminderItem[]>>('/reminders');
    return response.data;
  },

  markAsRead: async (id: number): Promise<ApiResponse<void>> => {
    const response = await api.put<ApiResponse<void>>(`/reminders/${id}/read`);
    return response.data;
  },
};

export const bundlesApi = {
  getBundles: async (): Promise<ApiResponse<DocumentBundleItem[]>> => {
    const response = await api.get<ApiResponse<DocumentBundleItem[]>>('/bundles');
    return response.data;
  },

  generateBundle: async (opportunityId?: number, bundleName?: string, documentIds?: number[]): Promise<ApiResponse<DocumentBundleItem>> => {
    const response = await api.post<ApiResponse<DocumentBundleItem>>('/bundles/generate', {
      opportunityId,
      bundleName,
      documentIds
    });
    return response.data;
  },

  getDownloadUrl: (bundleId: number): string => {
    const token = localStorage.getItem('docmind_token');
    return `/api/bundles/${bundleId}/download?token=${token}`;
  }
};

export const auditApi = {
  getLogs: async (): Promise<ApiResponse<AuditLogItem[]>> => {
    const response = await api.get<ApiResponse<AuditLogItem[]>>('/audit-logs');
    return response.data;
  },
};

export interface ChatResponsePayload {
  response: string;
  intent: string;
  actionType: string;
  actionData: Record<string, any>;
  suggestedPrompts: string[];
}

export const assistantApi = {
  chat: async (query: string): Promise<ApiResponse<ChatResponsePayload>> => {
    const response = await api.post<ApiResponse<ChatResponsePayload>>('/assistant/chat', { query });
    return response.data;
  },
};
