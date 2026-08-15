import React, { useState, useEffect } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { 
  LayoutDashboard, 
  FileText, 
  Briefcase, 
  Bot, 
  UserCheck, 
  ShieldCheck, 
  Settings, 
  LogOut, 
  Sparkles,
  Layers,
  Bell
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { remindersApi } from '../services/api';

export const Sidebar: React.FC = () => {
  const { user, logout } = useAuth();
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

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const navItems = [
    { label: 'Dashboard', path: '/dashboard', icon: LayoutDashboard },
    { label: 'My Documents', path: '/documents', icon: FileText },
    { label: 'Opportunities', path: '/opportunities', icon: Briefcase },
    { label: 'AI Assistant', path: '/assistant', icon: Bot, badge: 'AI' },
    { label: 'Digital Profile', path: '/profile', icon: UserCheck },
    { label: 'Document Bundles', path: '/bundles', icon: Layers },
    { label: 'Notifications & Alerts', path: '/notifications', icon: Bell, alertCount: unreadCount },
    { label: 'Security & Audit', path: '/security', icon: ShieldCheck },
    { label: 'Settings', path: '/settings', icon: Settings },
  ];

  return (
    <aside className="w-64 bg-slate-900/80 backdrop-blur-xl border-r border-slate-800 flex flex-col justify-between h-screen sticky top-0 z-30">
      <div>
        {/* Brand Header */}
        <div className="p-5 flex items-center gap-3 border-b border-slate-800/80">
          <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-indigo-600 via-indigo-500 to-purple-500 flex items-center justify-center shadow-lg shadow-indigo-500/20">
            <Sparkles className="w-5 h-5 text-white" />
          </div>
          <div>
            <h1 className="font-bold text-lg text-white tracking-tight flex items-center gap-1.5">
              DocMind <span className="text-xs px-1.5 py-0.5 rounded bg-indigo-500/20 text-indigo-400 font-semibold border border-indigo-500/30">AI</span>
            </h1>
            <p className="text-xs text-slate-400 font-medium">Digital Identity &amp; Opportunities</p>
          </div>
        </div>

        {/* Navigation Items */}
        <nav className="p-3 space-y-1 mt-2">
          {navItems.map((item) => {
            const Icon = item.icon;
            return (
              <NavLink
                key={item.path}
                to={item.path}
                className={({ isActive }) =>
                  `flex items-center justify-between px-3.5 py-2.5 rounded-xl font-medium text-sm transition-all duration-150 ${
                    isActive
                      ? 'bg-indigo-600/15 text-indigo-400 border border-indigo-500/30 shadow-sm'
                      : 'text-slate-400 hover:text-slate-200 hover:bg-slate-800/50'
                  }`
                }
              >
                <div className="flex items-center gap-3">
                  <Icon className="w-4 h-4" />
                  <span>{item.label}</span>
                </div>
                {item.alertCount !== undefined && item.alertCount > 0 ? (
                  <span className="px-2 py-0.5 rounded-full bg-rose-500 text-white text-[10px] font-extrabold animate-pulse shadow-md shadow-rose-500/40">
                    {item.alertCount}
                  </span>
                ) : item.badge ? (
                  <span className="text-[10px] font-extrabold px-1.5 py-0.5 rounded bg-indigo-500/20 text-indigo-300 border border-indigo-500/40">
                    {item.badge}
                  </span>
                ) : null}
              </NavLink>
            );
          })}
        </nav>
      </div>

      {/* User Footer */}
      <div className="p-4 border-t border-slate-800/80 bg-slate-900/50">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3 overflow-hidden">
            <div className="w-9 h-9 rounded-full bg-gradient-to-tr from-indigo-500 to-purple-600 flex items-center justify-center font-bold text-white text-sm shrink-0">
              {user?.fullName ? user.fullName.charAt(0).toUpperCase() : 'U'}
            </div>
            <div className="truncate">
              <p className="text-sm font-semibold text-white truncate">{user?.fullName || 'Hari Krishnan'}</p>
              <p className="text-xs text-slate-400 truncate">{user?.email || 'hari@example.com'}</p>
            </div>
          </div>
          <button
            onClick={handleLogout}
            title="Sign Out"
            className="p-2 text-slate-400 hover:text-rose-400 hover:bg-rose-500/10 rounded-lg transition-colors"
          >
            <LogOut className="w-4 h-4" />
          </button>
        </div>
      </div>
    </aside>
  );
};
