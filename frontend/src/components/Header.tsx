import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Search, Bell, ShieldCheck, Sparkles } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { remindersApi } from '../services/api';

export const Header: React.FC = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [unreadCount, setUnreadCount] = useState<number>(0);

  useEffect(() => {
    const fetchUnreadCount = async () => {
      try {
        const res = await remindersApi.getReminders();
        if (res.success && res.data) {
          const unread = res.data.filter((r) => !r.isRead).length;
          setUnreadCount(unread);
        }
      } catch {
        setUnreadCount(0);
      }
    };

    fetchUnreadCount();
    const interval = setInterval(fetchUnreadCount, 15000);
    return () => clearInterval(interval);
  }, []);

  return (
    <header className="h-16 border-b border-slate-800 bg-slate-950/60 backdrop-blur-md px-6 flex items-center justify-between sticky top-0 z-20">
      {/* Global Search Bar */}
      <div className="relative w-96">
        <Search className="w-4 h-4 text-slate-400 absolute left-3.5 top-1/2 -translate-y-1/2" />
        <input
          type="text"
          placeholder="Search documents, jobs, certificates, skills..."
          className="w-full bg-slate-900/80 border border-slate-800 rounded-xl pl-10 pr-4 py-2 text-sm text-slate-200 placeholder-slate-500 focus:outline-none focus:border-indigo-500/50 focus:ring-1 focus:ring-indigo-500/50 transition-all"
        />
      </div>

      {/* Header Actions */}
      <div className="flex items-center gap-4">
        {/* Security Vault Indicator */}
        <div className="flex items-center gap-2 px-3 py-1.5 rounded-full bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 text-xs font-medium">
          <ShieldCheck className="w-3.5 h-3.5" />
          <span>AES-256 Vault Encrypted</span>
        </div>

        {/* Notifications Button */}
        <button
          onClick={() => navigate('/notifications')}
          title="Notifications & Alerts"
          className="relative p-2 rounded-xl bg-slate-900 border border-slate-800 text-slate-400 hover:text-white transition-colors"
        >
          <Bell className="w-4 h-4 text-indigo-400" />
          {unreadCount > 0 && (
            <span className="absolute -top-1 -right-1 w-4 h-4 rounded-full bg-rose-500 text-white text-[9px] font-extrabold flex items-center justify-center animate-pulse">
              {unreadCount}
            </span>
          )}
        </button>

        {/* User Welcome Pill */}
        <div className="flex items-center gap-2 text-sm text-slate-300 bg-slate-900/60 border border-slate-800 px-3 py-1.5 rounded-xl">
          <Sparkles className="w-3.5 h-3.5 text-indigo-400" />
          <span>Welcome, <strong className="text-white font-semibold">{user?.fullName || 'Hari Krishnan'}</strong></span>
        </div>
      </div>
    </header>
  );
};
