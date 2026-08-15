import React, { useState, useEffect } from 'react';
import { 
  Bell, 
  AlertTriangle, 
  Calendar, 
  CheckCircle2, 
  Clock, 
  FileText, 
  Check
} from 'lucide-react';
import { ReminderItem } from '../types';
import { remindersApi } from '../services/api';

export const Notifications: React.FC = () => {
  const [reminders, setReminders] = useState<ReminderItem[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [filterPriority, setFilterPriority] = useState<string>('ALL');

  const fetchReminders = async () => {
    setLoading(true);
    try {
      const res = await remindersApi.getReminders();
      if (res.success && res.data) {
        setReminders(res.data);
      } else {
        setReminders([]);
      }
    } catch {
      setReminders([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchReminders();
  }, []);

  const handleMarkAsRead = async (id: number) => {
    try {
      await remindersApi.markAsRead(id);
      setReminders((prev) =>
        prev.map((r) => (r.id === id ? { ...r, isRead: true } : r))
      );
    } catch {
      setReminders((prev) =>
        prev.map((r) => (r.id === id ? { ...r, isRead: true } : r))
      );
    }
  };

  const filteredReminders = reminders.filter((r) => {
    if (filterPriority === 'ALL') return true;
    return r.priority === filterPriority;
  });

  const getPriorityBadge = (priority: string) => {
    switch (priority) {
      case 'URGENT':
        return 'bg-rose-500/20 text-rose-300 border-rose-500/40';
      case 'HIGH':
        return 'bg-amber-500/20 text-amber-300 border-amber-500/40';
      case 'MEDIUM':
        return 'bg-indigo-500/20 text-indigo-300 border-indigo-500/30';
      default:
        return 'bg-slate-800 text-slate-400 border-slate-700';
    }
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
            <Bell className="w-6 h-6 text-indigo-400" /> Expiry &amp; Scheduled Reminder Engine
          </h1>
          <p className="text-sm text-slate-400 mt-1">
            Automated alerts tracking document expiration dates, application deadlines, and vault tasks.
          </p>
        </div>

        <div className="flex items-center gap-2 text-xs text-slate-300 bg-slate-900 border border-slate-800 px-3.5 py-2 rounded-xl">
          <Clock className="w-4 h-4 text-emerald-400" />
          <span>Active Daily Cron Scheduler: <strong>ONLINE</strong></span>
        </div>
      </div>

      {/* Priority Filter Bar */}
      <div className="glass-panel p-4 rounded-2xl border border-slate-800 flex items-center justify-between gap-3">
        <div className="flex items-center gap-2 overflow-x-auto">
          {[
            { label: 'All Notifications', value: 'ALL' },
            { label: 'Urgent Expiries', value: 'URGENT' },
            { label: 'High Priority', value: 'HIGH' },
            { label: 'Medium Priority', value: 'MEDIUM' },
          ].map((tab) => (
            <button
              key={tab.value}
              onClick={() => setFilterPriority(tab.value)}
              className={`px-3.5 py-1.5 rounded-xl text-xs font-semibold whitespace-nowrap transition-all ${
                filterPriority === tab.value
                  ? 'bg-indigo-600 text-white shadow-md shadow-indigo-600/30'
                  : 'bg-slate-900 border border-slate-800 text-slate-400 hover:text-slate-200'
              }`}
            >
              {tab.label}
            </button>
          ))}
        </div>

        <span className="text-xs text-slate-400 font-medium shrink-0">
          Unread: <strong className="text-white">{reminders.filter(r => !r.isRead).length}</strong>
        </span>
      </div>

      {/* Reminders Feed */}
      {loading ? (
        <div className="p-12 text-center text-slate-400">
          <div className="w-8 h-8 border-4 border-indigo-500/20 border-t-indigo-500 rounded-full animate-spin mx-auto mb-3"></div>
          <p className="text-sm">Fetching Expiry Reminders &amp; Scheduled Notifications...</p>
        </div>
      ) : filteredReminders.length === 0 ? (
        <div className="glass-card p-12 rounded-2xl border border-slate-800 text-center space-y-3">
          <CheckCircle2 className="w-10 h-10 text-emerald-500 mx-auto" />
          <h3 className="text-lg font-bold text-white">All Clear! No Pending Expiries or Notifications</h3>
          <p className="text-sm text-slate-400 max-w-sm mx-auto">
            All your documents and application deadlines are up-to-date.
          </p>
        </div>
      ) : (
        <div className="space-y-4">
          {filteredReminders.map((r) => (
            <div
              key={r.id}
              className={`glass-card p-5 rounded-2xl border flex flex-col md:flex-row md:items-center justify-between gap-4 transition-all ${
                r.isRead
                  ? 'border-slate-800/80 opacity-75'
                  : r.priority === 'URGENT'
                  ? 'border-rose-500/40 bg-rose-500/5'
                  : 'border-indigo-500/40 bg-slate-900/60'
              }`}
            >
              <div className="flex items-start gap-4 min-w-0 flex-1">
                <div className={`p-3 rounded-2xl border shrink-0 ${
                  r.priority === 'URGENT' ? 'bg-rose-500/20 text-rose-400 border-rose-500/30' : 'bg-indigo-600/20 text-indigo-400 border-indigo-500/30'
                }`}>
                  {r.priority === 'URGENT' ? <AlertTriangle className="w-6 h-6" /> : <Bell className="w-6 h-6" />}
                </div>

                <div className="space-y-1 min-w-0">
                  <div className="flex items-center gap-2 flex-wrap">
                    <h3 className="font-bold text-white text-base truncate">{r.title}</h3>
                    <span className={`text-[10px] font-extrabold uppercase px-2 py-0.5 rounded border ${getPriorityBadge(r.priority)}`}>
                      {r.priority}
                    </span>
                  </div>

                  <p className="text-xs text-slate-300 leading-relaxed">{r.message}</p>

                  <div className="flex items-center gap-4 text-[11px] text-slate-400 pt-1">
                    <span className="flex items-center gap-1"><Calendar className="w-3 h-3 text-slate-500" /> Deadline: {r.reminderDate}</span>
                    {r.documentName && <span className="flex items-center gap-1"><FileText className="w-3 h-3 text-indigo-400" /> Doc: {r.documentName}</span>}
                  </div>
                </div>
              </div>

              <div className="flex items-center gap-3 shrink-0 self-end md:self-center">
                {!r.isRead ? (
                  <button
                    onClick={() => handleMarkAsRead(r.id)}
                    className="px-3.5 py-2 rounded-xl bg-slate-900 hover:bg-slate-800 border border-slate-800 text-slate-300 hover:text-white text-xs font-semibold flex items-center gap-1.5 transition-colors"
                  >
                    <Check className="w-4 h-4 text-emerald-400" /> Mark as Read
                  </button>
                ) : (
                  <span className="text-xs text-slate-500 flex items-center gap-1 font-medium">
                    <CheckCircle2 className="w-3.5 h-3.5 text-emerald-500" /> Read
                  </span>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
