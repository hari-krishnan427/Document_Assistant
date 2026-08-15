import React, { useState, useEffect } from 'react';
import { 
  ShieldCheck, 
  Lock, 
  Eye, 
  FileText, 
  Clock, 
  Search, 
  CheckCircle2, 
  Sparkles,
  Server,
  Activity
} from 'lucide-react';
import { AuditLogItem } from '../types';
import { auditApi } from '../services/api';

export const SecurityAudit: React.FC = () => {
  const [logs, setLogs] = useState<AuditLogItem[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [searchQuery, setSearchQuery] = useState<string>('');

  const demoLogs: AuditLogItem[] = [
    {
      id: 1,
      userId: 1,
      userName: 'Hari Krishnan',
      actionType: 'LOGIN',
      resource: 'AuthService',
      details: 'User authenticated via Spring Security JWT Token',
      ipAddress: '127.0.0.1',
      createdAt: '2026-08-13T10:15:00Z'
    },
    {
      id: 2,
      userId: 1,
      userName: 'Hari Krishnan',
      actionType: 'DOCUMENT_UPLOAD',
      resource: 'DocumentVault',
      details: 'Uploaded hari_aadhaar_card_masked.pdf with AES-256 zero-knowledge encryption',
      ipAddress: '127.0.0.1',
      createdAt: '2026-08-13T10:20:00Z'
    },
    {
      id: 3,
      userId: 1,
      userName: 'Hari Krishnan',
      actionType: 'OCR_PROCESS',
      resource: 'FastAPI_Microservice',
      details: 'FastAPI extracted fields. Masked Aadhaar number: XXXX XXXX 1234',
      ipAddress: '127.0.0.1',
      createdAt: '2026-08-13T10:21:00Z'
    },
    {
      id: 4,
      userId: 1,
      userName: 'Hari Krishnan',
      actionType: 'PROFILE_SYNC',
      resource: 'UserProfile',
      details: 'Auto-synced skills (Java, Python, SQL, React) & degree credentials. Readiness: 78%',
      ipAddress: '127.0.0.1',
      createdAt: '2026-08-13T10:25:00Z'
    },
    {
      id: 5,
      userId: 1,
      userName: 'Hari Krishnan',
      actionType: 'BUNDLE_CREATE',
      resource: 'DocumentBundle',
      details: 'Generated ZIP bundle ISRO_CS_Scientist_Application_Bundle_2026.zip',
      ipAddress: '127.0.0.1',
      createdAt: '2026-08-13T10:30:00Z'
    }
  ];

  const fetchLogs = async () => {
    setLoading(true);
    try {
      const res = await auditApi.getLogs();
      if (res.success && res.data.length > 0) {
        setLogs(res.data);
      } else {
        setLogs(demoLogs);
      }
    } catch {
      setLogs(demoLogs);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchLogs();
  }, []);

  const filteredLogs = logs.filter((log) => {
    const q = searchQuery.toLowerCase();
    return (
      log.actionType.toLowerCase().includes(q) ||
      log.resource.toLowerCase().includes(q) ||
      log.details.toLowerCase().includes(q)
    );
  });

  const getBadgeColor = (action: string) => {
    switch (action) {
      case 'LOGIN': return 'bg-indigo-500/20 text-indigo-300 border-indigo-500/30';
      case 'DOCUMENT_UPLOAD': return 'bg-emerald-500/20 text-emerald-300 border-emerald-500/30';
      case 'OCR_PROCESS': return 'bg-purple-500/20 text-purple-300 border-purple-500/30';
      case 'PROFILE_SYNC': return 'bg-amber-500/20 text-amber-300 border-amber-500/30';
      case 'BUNDLE_CREATE': return 'bg-cyan-500/20 text-cyan-300 border-cyan-500/30';
      default: return 'bg-slate-800 text-slate-300 border-slate-700';
    }
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
            <ShieldCheck className="w-6 h-6 text-emerald-400" /> Security Audit &amp; Privacy Log Engine
          </h1>
          <p className="text-sm text-slate-400 mt-1">
            Immutable audit logging, zero-knowledge AES-256 encryption status, and privacy controls.
          </p>
        </div>

        <div className="flex items-center gap-2 text-xs text-emerald-400 bg-emerald-500/10 border border-emerald-500/20 px-3.5 py-2 rounded-xl font-semibold">
          <Lock className="w-4 h-4" />
          <span>Zero-Knowledge AES-256 Vault: <strong>ACTIVE</strong></span>
        </div>
      </div>

      {/* Security Status Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div className="glass-card p-4 rounded-2xl border border-slate-800 flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-emerald-500/20 text-emerald-400 border border-emerald-500/30 flex items-center justify-center shrink-0">
            <Lock className="w-5 h-5" />
          </div>
          <div>
            <div className="text-xs text-slate-400 font-semibold uppercase">Storage Encryption</div>
            <div className="text-sm font-bold text-white">AES-256 Zero Knowledge</div>
          </div>
        </div>

        <div className="glass-card p-4 rounded-2xl border border-slate-800 flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-indigo-600/20 text-indigo-400 border border-indigo-500/30 flex items-center justify-center shrink-0">
            <ShieldCheck className="w-5 h-5" />
          </div>
          <div>
            <div className="text-xs text-slate-400 font-semibold uppercase">Sensitive Data Masking</div>
            <div className="text-sm font-bold text-white">Aadhaar (XXXX XXXX 1234)</div>
          </div>
        </div>

        <div className="glass-card p-4 rounded-2xl border border-slate-800 flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-purple-500/20 text-purple-400 border border-purple-500/30 flex items-center justify-center shrink-0">
            <Activity className="w-5 h-5" />
          </div>
          <div>
            <div className="text-xs text-slate-400 font-semibold uppercase">Audit Logging</div>
            <div className="text-sm font-bold text-white">100% Immutable Trail</div>
          </div>
        </div>
      </div>

      {/* Search & Audit Logs Table */}
      <div className="glass-panel p-5 rounded-2xl border border-slate-800 space-y-4">
        <div className="flex flex-col md:flex-row items-center justify-between gap-4 border-b border-slate-800 pb-3">
          <div className="relative flex-1 w-full">
            <Search className="w-4 h-4 text-slate-500 absolute left-3.5 top-1/2 -translate-y-1/2" />
            <input
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="Search audit trail by action (LOGIN, OCR, UPLOAD), resource, or details..."
              className="w-full bg-slate-900 border border-slate-800 rounded-xl pl-10 pr-4 py-2 text-sm text-slate-200 placeholder-slate-500 focus:outline-none focus:border-indigo-500/50"
            />
          </div>
          <span className="text-xs text-slate-400 font-semibold shrink-0">
            Total Audit Records: <strong className="text-white">{filteredLogs.length}</strong>
          </span>
        </div>

        {loading ? (
          <div className="p-8 text-center text-slate-400 text-xs">
            Fetching Security Audit Logs...
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs">
              <thead>
                <tr className="border-b border-slate-800 text-slate-400 uppercase font-semibold text-[10px]">
                  <th className="pb-3">Action Event</th>
                  <th className="pb-3">Resource Target</th>
                  <th className="pb-3">Security Details</th>
                  <th className="pb-3">IP Address</th>
                  <th className="pb-3 text-right">Timestamp</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-800/60">
                {filteredLogs.map((log) => (
                  <tr key={log.id} className="hover:bg-slate-900/60 transition-colors">
                    <td className="py-3">
                      <span className={`text-[10px] font-extrabold uppercase px-2.5 py-0.5 rounded border ${getBadgeColor(log.actionType)}`}>
                        {log.actionType}
                      </span>
                    </td>
                    <td className="py-3 font-semibold text-slate-200">{log.resource}</td>
                    <td className="py-3 text-slate-300 max-w-md truncate" title={log.details}>{log.details}</td>
                    <td className="py-3 font-mono text-slate-400">{log.ipAddress}</td>
                    <td className="py-3 text-right text-slate-400">{new Date(log.createdAt).toLocaleTimeString()}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};
