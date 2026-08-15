import React, { useState } from 'react';
import { 
  Settings as SettingsIcon, 
  ShieldCheck, 
  Lock, 
  Eye, 
  Bell, 
  Cpu, 
  Sparkles, 
  CheckCircle2, 
  Save
} from 'lucide-react';

export const Settings: React.FC = () => {
  const [aesEncryption, setAesEncryption] = useState<boolean>(true);
  const [aadhaarMasking, setAadhaarMasking] = useState<boolean>(true);
  const [autoOpportunitySync, setAutoOpportunitySync] = useState<boolean>(true);
  const [auditLogging, setAuditLogging] = useState<boolean>(true);
  const [expiryAlerts, setExpiryAlerts] = useState<boolean>(true);
  const [savedMessage, setSavedMessage] = useState<string>('');

  const handleSaveSettings = (e: React.FormEvent) => {
    e.preventDefault();
    setSavedMessage('Security & privacy settings saved successfully!');
    setTimeout(() => setSavedMessage(''), 4000);
  };

  return (
    <div className="space-y-6 max-w-4xl mx-auto">
      {/* Header */}
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
            <SettingsIcon className="w-6 h-6 text-indigo-400" /> Privacy Controls &amp; Settings
          </h1>
          <p className="text-sm text-slate-400 mt-1">
            Configure zero-knowledge encryption, sensitive data masking, and AI opportunity preferences.
          </p>
        </div>

        <div className="flex items-center gap-2 text-xs text-indigo-300 bg-indigo-500/10 border border-indigo-500/20 px-3.5 py-2 rounded-xl font-semibold">
          <Sparkles className="w-4 h-4 text-indigo-400" />
          <span>Placement Demo Showcase Ready</span>
        </div>
      </div>

      {savedMessage && (
        <div className="p-3.5 rounded-xl bg-emerald-500/10 border border-emerald-500/30 text-emerald-400 text-xs font-semibold flex items-center gap-2 animate-fade-in">
          <CheckCircle2 className="w-4 h-4" /> {savedMessage}
        </div>
      )}

      <form onSubmit={handleSaveSettings} className="space-y-6">
        {/* Security & Vault Encryption Controls */}
        <div className="glass-panel p-6 rounded-2xl border border-slate-800 space-y-5">
          <div className="border-b border-slate-800 pb-3">
            <h2 className="text-base font-bold text-white flex items-center gap-2">
              <ShieldCheck className="w-5 h-5 text-emerald-400" /> Zero-Knowledge Security &amp; Masking
            </h2>
            <p className="text-xs text-slate-400 mt-0.5">Manage how sensitive identity numbers and vault files are handled.</p>
          </div>

          <div className="space-y-4">
            <div className="flex items-center justify-between p-3.5 rounded-xl bg-slate-900/80 border border-slate-800">
              <div className="space-y-0.5">
                <h4 className="font-bold text-white text-xs flex items-center gap-1.5">
                  <Lock className="w-4 h-4 text-emerald-400" /> AES-256 Vault Encryption
                </h4>
                <p className="text-[11px] text-slate-400">Encrypt documents at rest in local storage using AES-256 keys.</p>
              </div>
              <button
                type="button"
                onClick={() => setAesEncryption(!aesEncryption)}
                className={`w-12 h-6 rounded-full transition-colors relative ${aesEncryption ? 'bg-indigo-600' : 'bg-slate-800'}`}
              >
                <div className={`w-4 h-4 rounded-full bg-white absolute top-1 transition-transform ${aesEncryption ? 'right-1' : 'left-1'}`} />
              </button>
            </div>

            <div className="flex items-center justify-between p-3.5 rounded-xl bg-slate-900/80 border border-slate-800">
              <div className="space-y-0.5">
                <h4 className="font-bold text-white text-xs flex items-center gap-1.5">
                  <Eye className="w-4 h-4 text-indigo-400" /> Automatic Sensitive Identity Masking
                </h4>
                <p className="text-[11px] text-slate-400">Mask Aadhaar Card numbers to format (XXXX XXXX 1234) before saving.</p>
              </div>
              <button
                type="button"
                onClick={() => setAadhaarMasking(!aadhaarMasking)}
                className={`w-12 h-6 rounded-full transition-colors relative ${aadhaarMasking ? 'bg-indigo-600' : 'bg-slate-800'}`}
              >
                <div className={`w-4 h-4 rounded-full bg-white absolute top-1 transition-transform ${aadhaarMasking ? 'right-1' : 'left-1'}`} />
              </button>
            </div>
          </div>
        </div>

        {/* AI & Discovery Preferences */}
        <div className="glass-panel p-6 rounded-2xl border border-slate-800 space-y-5">
          <div className="border-b border-slate-800 pb-3">
            <h2 className="text-base font-bold text-white flex items-center gap-2">
              <Cpu className="w-5 h-5 text-indigo-400" /> AI Opportunity &amp; Expiry Engine
            </h2>
            <p className="text-xs text-slate-400 mt-0.5">Configure automatic opportunity discovery and daily cron expiry checks.</p>
          </div>

          <div className="space-y-4">
            <div className="flex items-center justify-between p-3.5 rounded-xl bg-slate-900/80 border border-slate-800">
              <div className="space-y-0.5">
                <h4 className="font-bold text-white text-xs flex items-center gap-1.5">
                  <Sparkles className="w-4 h-4 text-purple-400" /> Active Placement Opportunity Discovery
                </h4>
                <p className="text-[11px] text-slate-400">Continuously search jobs, ISRO/GATE exams, and internships across India &amp; South India.</p>
              </div>
              <button
                type="button"
                onClick={() => setAutoOpportunitySync(!autoOpportunitySync)}
                className={`w-12 h-6 rounded-full transition-colors relative ${autoOpportunitySync ? 'bg-indigo-600' : 'bg-slate-800'}`}
              >
                <div className={`w-4 h-4 rounded-full bg-white absolute top-1 transition-transform ${autoOpportunitySync ? 'right-1' : 'left-1'}`} />
              </button>
            </div>

            <div className="flex items-center justify-between p-3.5 rounded-xl bg-slate-900/80 border border-slate-800">
              <div className="space-y-0.5">
                <h4 className="font-bold text-white text-xs flex items-center gap-1.5">
                  <Bell className="w-4 h-4 text-amber-400" /> Daily Cron Expiry Scheduler
                </h4>
                <p className="text-[11px] text-slate-400">Send reminder alerts 30 days prior to document expiration dates.</p>
              </div>
              <button
                type="button"
                onClick={() => setExpiryAlerts(!expiryAlerts)}
                className={`w-12 h-6 rounded-full transition-colors relative ${expiryAlerts ? 'bg-indigo-600' : 'bg-slate-800'}`}
              >
                <div className={`w-4 h-4 rounded-full bg-white absolute top-1 transition-transform ${expiryAlerts ? 'right-1' : 'left-1'}`} />
              </button>
            </div>
          </div>
        </div>

        <div className="flex items-center justify-end">
          <button
            type="submit"
            className="px-6 py-3 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-semibold text-xs shadow-lg shadow-indigo-600/30 flex items-center gap-2 transition-all"
          >
            <Save className="w-4 h-4" />
            <span>Save Preferences</span>
          </button>
        </div>
      </form>
    </div>
  );
};
