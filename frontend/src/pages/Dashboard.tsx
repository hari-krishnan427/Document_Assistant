import React, { useState, useEffect } from 'react';
import { 
  FileText, 
  Briefcase, 
  AlertTriangle, 
  CheckCircle2, 
  Sparkles, 
  ArrowUpRight, 
  Layers, 
  ShieldAlert,
  Bot,
  Plus
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { DocumentItem, OpportunityItem, ReminderItem } from '../types';
import { documentsApi, opportunitiesApi, remindersApi, profileApi } from '../services/api';

export const Dashboard: React.FC = () => {
  const { user } = useAuth();
  const [documents, setDocuments] = useState<DocumentItem[]>([]);
  const [opportunities, setOpportunities] = useState<OpportunityItem[]>([]);
  const [reminders, setReminders] = useState<ReminderItem[]>([]);
  const [readinessScore, setReadinessScore] = useState<number>(0);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    const fetchDashboardData = async () => {
      setLoading(true);
      try {
        const [docsRes, oppsRes, remsRes, profRes] = await Promise.all([
          documentsApi.getDocuments().catch(() => null),
          opportunitiesApi.getOpportunities().catch(() => null),
          remindersApi.getReminders().catch(() => null),
          profileApi.getProfile().catch(() => null),
        ]);

        const docs = (docsRes && docsRes.success && docsRes.data) ? docsRes.data : [];
        setDocuments(docs);

        const opps = (oppsRes && oppsRes.success && oppsRes.data) ? oppsRes.data : [];
        setOpportunities(opps);

        const rems = (remsRes && remsRes.success && remsRes.data) ? remsRes.data : [];
        setReminders(rems);

        if (docs.length === 0) {
          setReadinessScore(0);
        } else if (profRes && profRes.success && profRes.data && profRes.data.readinessScore) {
          setReadinessScore(profRes.data.readinessScore);
        } else {
          setReadinessScore(Math.min(100, Math.round((docs.length / 5) * 100)));
        }
      } catch {
        setDocuments([]);
        setOpportunities([]);
        setReminders([]);
        setReadinessScore(0);
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardData();
  }, []);

  const expiringDocs = documents.filter((d) => d.status === 'EXPIRING_SOON');

  return (
    <div className="space-y-6">
      {/* Welcome Banner & Digital Readiness Score */}
      <div className="glass-panel p-6 rounded-2xl border border-slate-800 bg-gradient-to-r from-slate-900 via-indigo-950/40 to-slate-900 relative overflow-hidden">
        <div className="flex flex-col md:flex-row items-start md:items-center justify-between gap-6 relative z-10">
          <div>
            <div className="flex items-center gap-2 mb-2">
              <span className="px-2.5 py-1 rounded-full bg-indigo-500/10 border border-indigo-500/20 text-indigo-400 text-xs font-semibold flex items-center gap-1.5">
                <Sparkles className="w-3.5 h-3.5" /> DocMind AI Digital Identity Engine
              </span>
            </div>
            <h1 className="text-2xl font-bold text-white tracking-tight">
              Good day, {user?.fullName || 'User'} 👋
            </h1>
            <p className="text-slate-400 text-sm mt-1 max-w-xl">
              DocMind AI has {documents.length} {documents.length === 1 ? 'document' : 'documents'} in your vault, monitoring {opportunities.length} live career opportunities, and tracking {expiringDocs.length} upcoming expiries.
            </p>
          </div>

          {/* Readiness Gauge */}
          <div className="flex items-center gap-4 bg-slate-900/90 p-4 rounded-xl border border-slate-800 shrink-0">
            <div className="relative w-16 h-16 flex items-center justify-center">
              <svg className="w-full h-full transform -rotate-90">
                <circle cx="32" cy="32" r="26" stroke="currentColor" strokeWidth="6" className="text-slate-800" fill="transparent" />
                <circle
                  cx="32"
                  cy="32"
                  r="26"
                  stroke="currentColor"
                  strokeWidth="6"
                  className="text-indigo-500"
                  strokeDasharray={163}
                  strokeDashoffset={163 - (163 * readinessScore) / 100}
                  strokeLinecap="round"
                  fill="transparent"
                />
              </svg>
              <span className="absolute font-bold text-base text-white">{readinessScore}%</span>
            </div>
            <div>
              <p className="text-xs font-semibold text-slate-400 uppercase tracking-wider">Digital Readiness</p>
              <p className="text-sm font-bold text-emerald-400 mt-0.5">
                {readinessScore > 50 ? 'High Readiness' : 'Initial Setup'}
              </p>
              <p className="text-xs text-slate-500">{documents.length} documents uploaded</p>
            </div>
          </div>
        </div>
      </div>

      {documents.length === 0 && (
        <div className="p-4 rounded-2xl bg-indigo-600/10 border border-indigo-500/30 flex items-center justify-between gap-4">
          <div className="flex items-center gap-3">
            <div className="p-2.5 rounded-xl bg-indigo-600/20 text-indigo-400 shrink-0">
              <Plus className="w-5 h-5" />
            </div>
            <div>
              <h4 className="text-sm font-bold text-white">Upload Your First Document to Unlock AI Matching</h4>
              <p className="text-xs text-slate-400">Upload your resume or degree certificate to calculate personalized job match scores and eligibility.</p>
            </div>
          </div>
          <a
            href="/documents"
            className="px-4 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-semibold text-xs transition-all shrink-0 shadow-md shadow-indigo-600/20"
          >
            Upload Now
          </a>
        </div>
      )}

      {/* Metric Cards Grid */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-5">
        <div className="glass-card p-5 rounded-2xl border border-slate-800/80">
          <div className="flex items-center justify-between text-slate-400 mb-3">
            <span className="text-xs font-semibold uppercase tracking-wider">Total Documents</span>
            <FileText className="w-5 h-5 text-indigo-400" />
          </div>
          <p className="text-3xl font-extrabold text-white">{documents.length}</p>
          <p className="text-xs text-slate-400 mt-1 flex items-center gap-1">
            <CheckCircle2 className="w-3.5 h-3.5 text-emerald-400" /> Verified Vault Encrypted
          </p>
        </div>

        <div className="glass-card p-5 rounded-2xl border border-slate-800/80">
          <div className="flex items-center justify-between text-slate-400 mb-3">
            <span className="text-xs font-semibold uppercase tracking-wider">Expiring Soon</span>
            <AlertTriangle className="w-5 h-5 text-amber-400" />
          </div>
          <p className="text-3xl font-extrabold text-amber-400">{expiringDocs.length}</p>
          <p className="text-xs text-slate-400 mt-1">
            {expiringDocs.length > 0 ? 'Requires renewal action' : 'All documents up to date'}
          </p>
        </div>

        <div className="glass-card p-5 rounded-2xl border border-slate-800/80">
          <div className="flex items-center justify-between text-slate-400 mb-3">
            <span className="text-xs font-semibold uppercase tracking-wider">Matching Opportunities</span>
            <Briefcase className="w-5 h-5 text-indigo-400" />
          </div>
          <p className="text-3xl font-extrabold text-white">{opportunities.length}</p>
          <p className="text-xs text-indigo-300 mt-1 font-medium">
            {documents.length === 0 ? 'Pending Vault Verification' : 'Live Opportunities'}
          </p>
        </div>

        <div className="glass-card p-5 rounded-2xl border border-slate-800/80">
          <div className="flex items-center justify-between text-slate-400 mb-3">
            <span className="text-xs font-semibold uppercase tracking-wider">Reminders / Alerts</span>
            <Layers className="w-5 h-5 text-rose-400" />
          </div>
          <p className="text-3xl font-extrabold text-rose-400">{reminders.length}</p>
          <p className="text-xs text-slate-400 mt-1">Active notifications</p>
        </div>
      </div>

      {/* Main Content Layout */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Top Matches Section */}
        <div className="lg:col-span-2 space-y-4">
          <div className="flex items-center justify-between">
            <h2 className="text-lg font-bold text-white flex items-center gap-2">
              <Briefcase className="w-5 h-5 text-indigo-400" />
              Live Real-World Opportunities
            </h2>
            <a href="/opportunities" className="text-xs text-indigo-400 hover:text-indigo-300 font-semibold flex items-center gap-1">
              View All {opportunities.length} <ArrowUpRight className="w-3.5 h-3.5" />
            </a>
          </div>

          {opportunities.length === 0 ? (
            <div className="glass-card p-8 rounded-2xl border border-slate-800 text-center space-y-3">
              <Briefcase className="w-8 h-8 text-slate-600 mx-auto" />
              <h3 className="text-base font-bold text-white">No Opportunities Loaded</h3>
              <p className="text-xs text-slate-400 max-w-sm mx-auto">
                Explore the Opportunities section to search live career opportunities.
              </p>
            </div>
          ) : (
            <div className="space-y-3">
              {opportunities.slice(0, 3).map((opp, idx) => (
                <div key={idx} className="glass-card p-5 rounded-2xl border border-slate-800 hover:border-indigo-500/40 flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
                  <div className="space-y-1">
                    <div className="flex items-center gap-2">
                      <span className="text-[10px] font-extrabold uppercase px-2 py-0.5 rounded bg-indigo-500/20 text-indigo-300 border border-indigo-500/30">
                        {opp.opportunityType ? opp.opportunityType.replace('_', ' ') : 'JOB'}
                      </span>
                      <span className="text-xs text-slate-400">• {opp.organization}</span>
                    </div>
                    <h3 className="font-bold text-white text-base">{opp.title}</h3>
                    <div className="flex items-center gap-4 text-xs text-slate-400">
                      <span>Location: {opp.location}</span>
                    </div>
                  </div>

                  <div className="flex items-center gap-3 shrink-0 self-end md:self-center">
                    <div className="text-right">
                      <span className="text-xs text-slate-400 block">AI Match</span>
                      <span className="text-xs font-bold text-indigo-300">
                        {documents.length === 0 ? 'Pending Doc Upload' : `${opp.matchScore || 75}%`}
                      </span>
                    </div>
                    <a 
                      href="/opportunities" 
                      className="px-4 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-semibold text-xs transition-all shadow-md shadow-indigo-600/20"
                    >
                      View Details
                    </a>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Expiring Docs & Quick Assistant */}
        <div className="space-y-6">
          <div className="glass-card p-5 rounded-2xl border border-slate-800">
            <div className="flex items-center gap-2 mb-3">
              <ShieldAlert className="w-5 h-5 text-amber-400" />
              <h3 className="font-bold text-amber-400 text-sm">Expiring Document Alerts</h3>
            </div>
            {expiringDocs.length === 0 ? (
              <div className="p-4 rounded-xl bg-slate-900/60 border border-slate-800 text-center text-xs text-slate-400">
                No expiring documents. All uploaded documents are active.
              </div>
            ) : (
              <div className="space-y-3">
                {expiringDocs.map((doc, idx) => (
                  <div key={idx} className="p-3 rounded-xl bg-slate-900/80 border border-slate-800 flex items-center justify-between">
                    <div>
                      <p className="text-xs font-bold text-white">{doc.fileName}</p>
                      <p className="text-[11px] text-slate-400">Expires: {doc.expiryDate || 'N/A'}</p>
                    </div>
                    <span className="text-xs font-bold text-amber-400 bg-amber-500/10 px-2 py-1 rounded border border-amber-500/20">
                      Expiring Soon
                    </span>
                  </div>
                ))}
              </div>
            )}
          </div>

          <div className="glass-card p-5 rounded-2xl border border-slate-800 bg-gradient-to-b from-indigo-950/30 to-slate-900">
            <div className="flex items-center gap-2 mb-3">
              <Bot className="w-5 h-5 text-indigo-400" />
              <h3 className="font-bold text-white text-sm">Ask DocMind AI</h3>
            </div>
            <p className="text-xs text-slate-400 mb-4">
              Ask questions about your uploaded documents, expiry dates, or application status.
            </p>
            <div className="space-y-2">
              <a
                href="/documents"
                className="block p-2.5 rounded-xl bg-slate-900/80 hover:bg-indigo-600/20 border border-slate-800 hover:border-indigo-500/30 text-xs text-indigo-300 font-medium transition-all flex items-center justify-between"
              >
                <span>Upload a document to vault</span>
                <Plus className="w-3.5 h-3.5 text-indigo-400" />
              </a>
              <a
                href="/assistant"
                className="block p-2.5 rounded-xl bg-slate-900/80 hover:bg-indigo-600/20 border border-slate-800 hover:border-indigo-500/30 text-xs text-indigo-300 font-medium transition-all"
              >
                Open AI Document Assistant →
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
