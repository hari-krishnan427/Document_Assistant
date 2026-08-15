import React, { useState, useEffect } from 'react';
import { 
  FolderArchive, 
  Download, 
  Sparkles, 
  CheckCircle2, 
  AlertTriangle, 
  Package, 
  Layers
} from 'lucide-react';
import { DocumentBundleItem, OpportunityItem, DocumentItem } from '../types';
import { bundlesApi, opportunitiesApi, documentsApi } from '../services/api';

export const DocumentBundles: React.FC = () => {
  const [bundles, setBundles] = useState<DocumentBundleItem[]>([]);
  const [opportunities, setOpportunities] = useState<OpportunityItem[]>([]);
  const [documents, setDocuments] = useState<DocumentItem[]>([]);
  const [selectedOppId, setSelectedOppId] = useState<number | null>(null);
  const [customBundleName, setCustomBundleName] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(true);
  const [isGenerating, setIsGenerating] = useState<boolean>(false);
  const [successMessage, setSuccessMessage] = useState<string>('');

  const fetchData = async () => {
    setLoading(true);
    try {
      const [bRes, oRes, dRes] = await Promise.all([
        bundlesApi.getBundles().catch(() => null),
        opportunitiesApi.getOpportunities().catch(() => null),
        documentsApi.getDocuments().catch(() => null)
      ]);
      if (bRes && bRes.success && bRes.data) setBundles(bRes.data);
      else setBundles([]);

      if (oRes && oRes.success && oRes.data) {
        setOpportunities(oRes.data);
        if (oRes.data.length > 0) setSelectedOppId(oRes.data[0].id);
      } else setOpportunities([]);

      if (dRes && dRes.success && dRes.data) setDocuments(dRes.data);
      else setDocuments([]);
    } catch {
      setBundles([]);
      setOpportunities([]);
      setDocuments([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleGenerateBundle = async (e: React.FormEvent) => {
    e.preventDefault();
    if (documents.length === 0) {
      alert("No documents found in your vault. Please upload documents before generating a ZIP bundle.");
      return;
    }

    setIsGenerating(true);
    setSuccessMessage('');

    try {
      const selectedOpp = opportunities.find(o => o.id === selectedOppId);
      const name = customBundleName || `${selectedOpp ? selectedOpp.organization : 'General'}_Application_Bundle.zip`;

      const res = await bundlesApi.generateBundle(
        selectedOppId || undefined,
        name
      );

      if (res.success && res.data) {
        setBundles((prev) => [res.data, ...prev]);
        setSuccessMessage(`Successfully packaged ZIP bundle: ${name}`);
      }
    } catch (err: any) {
      alert(err.message || 'Failed to generate bundle.');
    } finally {
      setIsGenerating(false);
      setCustomBundleName('');
      setTimeout(() => setSuccessMessage(''), 5000);
    }
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
            <FolderArchive className="w-6 h-6 text-indigo-400" /> Missing Document Detector &amp; Bundle Generator
          </h1>
          <p className="text-sm text-slate-400 mt-1">
            Detect missing criteria for opportunities and compile verified document packages into single ZIP archives.
          </p>
        </div>

        <div className="flex items-center gap-2 text-xs text-slate-300 bg-slate-900 border border-slate-800 px-3.5 py-2 rounded-xl">
          <Layers className="w-4 h-4 text-purple-400" />
          <span>Automated ZIP Compression Engine</span>
        </div>
      </div>

      {successMessage && (
        <div className="p-3.5 rounded-xl bg-emerald-500/10 border border-emerald-500/30 text-emerald-400 text-xs font-semibold flex items-center gap-2 animate-fade-in">
          <Sparkles className="w-4 h-4" /> {successMessage}
        </div>
      )}

      {/* Target Opportunity & Missing Document Detector Box */}
      <div className="glass-panel p-6 rounded-2xl border border-slate-800 space-y-6">
        <div className="border-b border-slate-800 pb-4">
          <h2 className="text-lg font-bold text-white flex items-center gap-2">
            <Sparkles className="w-5 h-5 text-indigo-400" /> Target Opportunity &amp; Detector
          </h2>
          <p className="text-xs text-slate-400 mt-1">Select an active opportunity to analyze document completeness and package your application bundle.</p>
        </div>

        <form onSubmit={handleGenerateBundle} className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-semibold text-slate-300 uppercase tracking-wider mb-2">
                Target Opportunity
              </label>
              <select
                value={selectedOppId || ''}
                onChange={(e) => setSelectedOppId(Number(e.target.value))}
                className="w-full bg-slate-900 border border-slate-800 rounded-xl px-3.5 py-2.5 text-sm text-slate-200 focus:outline-none focus:border-indigo-500"
              >
                {opportunities.length === 0 ? (
                  <option value="">No opportunities loaded</option>
                ) : (
                  opportunities.map((opp) => (
                    <option key={opp.id} value={opp.id}>
                      {opp.title} ({opp.organization})
                    </option>
                  ))
                )}
              </select>
            </div>

            <div>
              <label className="block text-xs font-semibold text-slate-300 uppercase tracking-wider mb-2">
                Custom ZIP File Name (Optional)
              </label>
              <input
                type="text"
                value={customBundleName}
                onChange={(e) => setCustomBundleName(e.target.value)}
                placeholder="e.g. My_Application_Bundle.zip"
                className="w-full bg-slate-900 border border-slate-800 rounded-xl px-3.5 py-2.5 text-sm text-slate-200 placeholder-slate-500 focus:outline-none focus:border-indigo-500"
              />
            </div>
          </div>

          {/* Missing Document Detector Breakdown Card */}
          <div className="p-4 rounded-xl bg-slate-900/90 border border-slate-800 space-y-3">
            <h4 className="text-xs font-bold uppercase tracking-wider text-slate-400 flex items-center justify-between">
              <span>Required Application Document Check</span>
              <span className="text-emerald-400 font-semibold flex items-center gap-1">
                <CheckCircle2 className="w-3.5 h-3.5" /> {documents.length} Document(s) Ready in Vault
              </span>
            </h4>

            {documents.length === 0 ? (
              <div className="p-4 rounded-lg bg-slate-950 border border-amber-500/30 text-xs text-amber-400 flex items-center gap-2">
                <AlertTriangle className="w-4 h-4 shrink-0" />
                <span>No documents in vault yet. Upload your resume or identity certificates to package an application ZIP bundle.</span>
              </div>
            ) : (
              <div className="grid grid-cols-1 md:grid-cols-2 gap-3 text-xs">
                {documents.map((doc) => (
                  <div key={doc.id} className="p-3 rounded-lg bg-slate-950 border border-emerald-500/30 flex items-center justify-between">
                    <div className="flex items-center gap-2">
                      <CheckCircle2 className="w-4 h-4 text-emerald-400 shrink-0" />
                      <span className="text-slate-200 font-medium">{doc.fileName}</span>
                    </div>
                    <span className="text-[10px] text-emerald-400 font-extrabold bg-emerald-500/10 px-2 py-0.5 rounded border border-emerald-500/20">
                      {doc.category}
                    </span>
                  </div>
                ))}
              </div>
            )}
          </div>

          <div className="flex items-center justify-end">
            <button
              type="submit"
              disabled={isGenerating || documents.length === 0}
              className="px-6 py-3 rounded-xl bg-gradient-to-r from-indigo-600 to-purple-600 hover:from-indigo-500 hover:to-purple-500 text-white font-semibold text-xs shadow-lg shadow-indigo-600/30 flex items-center gap-2 transition-all disabled:opacity-50"
            >
              <Package className="w-4 h-4" />
              <span>{isGenerating ? 'Compressing & Generating ZIP...' : 'Generate Application ZIP Bundle'}</span>
            </button>
          </div>
        </form>
      </div>

      {/* Downloadable Bundle Archives History */}
      <div className="glass-panel p-6 rounded-2xl border border-slate-800 space-y-4">
        <div className="border-b border-slate-800 pb-3 flex items-center justify-between">
          <h3 className="font-bold text-white text-base flex items-center gap-2">
            <FolderArchive className="w-5 h-5 text-indigo-400" /> Generated Document Bundles
          </h3>
          <span className="text-xs text-slate-400">{bundles.length} Archives Available</span>
        </div>

        {loading ? (
          <div className="p-8 text-center text-slate-400 text-xs">
            Loading Generated Bundles...
          </div>
        ) : bundles.length === 0 ? (
          <div className="p-8 text-center text-slate-500 text-xs">
            No document bundles generated yet. Select an opportunity above to package your files.
          </div>
        ) : (
          <div className="space-y-3">
            {bundles.map((bundle) => (
              <div
                key={bundle.id}
                className="p-4 rounded-xl bg-slate-900/80 border border-slate-800 hover:border-indigo-500/40 flex flex-col md:flex-row md:items-center justify-between gap-4 transition-all"
              >
                <div className="flex items-start gap-3.5 min-w-0">
                  <div className="w-10 h-10 rounded-xl bg-indigo-600/20 text-indigo-400 border border-indigo-500/30 flex items-center justify-center shrink-0 mt-0.5">
                    <FolderArchive className="w-5 h-5" />
                  </div>
                  <div className="min-w-0 space-y-1">
                    <h4 className="font-bold text-white text-sm truncate">{bundle.bundleName}</h4>
                    <p className="text-xs text-indigo-300 font-medium truncate">{bundle.opportunityTitle || 'General Package'}</p>
                    <div className="flex items-center gap-3 text-[11px] text-slate-400">
                      <span>Files Packaged: <strong>{bundle.fileCount} Documents</strong></span>
                      <span>Created: <strong>{new Date(bundle.createdAt).toLocaleDateString()}</strong></span>
                    </div>
                  </div>
                </div>

                <a
                  href={bundlesApi.getDownloadUrl(bundle.id)}
                  download
                  className="px-4 py-2.5 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-semibold text-xs flex items-center gap-2 shadow-md shadow-indigo-600/20 shrink-0 self-start md:self-center"
                >
                  <Download className="w-4 h-4" /> Download ZIP
                </a>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};
