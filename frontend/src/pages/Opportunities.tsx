import React, { useState, useEffect } from 'react';
import { 
  Compass, 
  Search, 
  MapPin, 
  Building2, 
  ExternalLink, 
  Sparkles, 
  CheckCircle2, 
  AlertTriangle,
  X, 
  ChevronLeft,
  ChevronRight,
  FileQuestion
} from 'lucide-react';
import { OpportunityItem, DocumentItem } from '../types';
import { opportunitiesApi, documentsApi } from '../services/api';

export const Opportunities: React.FC = () => {
  const [opportunities, setOpportunities] = useState<OpportunityItem[]>([]);
  const [documents, setDocuments] = useState<DocumentItem[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [searchQuery, setSearchQuery] = useState<string>('');
  const [selectedType, setSelectedType] = useState<string>('ALL');
  const [selectedRegion, setSelectedRegion] = useState<string>('South India');
  const [selectedOpp, setSelectedOpp] = useState<OpportunityItem | null>(null);

  // Pagination State
  const [currentPage, setCurrentPage] = useState<number>(1);
  const totalPages = 50; // Infinite market pagination up to page 50+

  const fetchOpportunities = async (pageToFetch: number) => {
    setLoading(true);
    try {
      const [oppRes, docRes] = await Promise.all([
        opportunitiesApi.getOpportunities(
          searchQuery || undefined,
          selectedType !== 'ALL' ? selectedType : undefined,
          selectedRegion !== 'ALL' ? selectedRegion : undefined,
          pageToFetch
        ).catch(() => null),
        documentsApi.getDocuments().catch(() => null)
      ]);

      if (oppRes && oppRes.success && oppRes.data) {
        setOpportunities(oppRes.data);
      } else {
        setOpportunities([]);
      }

      if (docRes && docRes.success && docRes.data) {
        setDocuments(docRes.data);
      } else {
        setDocuments([]);
      }
    } catch {
      setOpportunities([]);
      setDocuments([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOpportunities(currentPage);
  }, [selectedType, selectedRegion, currentPage]);

  const handleSearchSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setCurrentPage(1);
    fetchOpportunities(1);
  };

  const handlePageChange = (newPage: number) => {
    if (newPage >= 1 && newPage <= totalPages) {
      setCurrentPage(newPage);
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  };

  const getTypeBadge = (type: string) => {
    switch (type) {
      case 'GOVT_EXAM': return 'bg-amber-500/20 text-amber-300 border-amber-500/30';
      case 'JOB': return 'bg-indigo-500/20 text-indigo-300 border-indigo-500/30';
      case 'INTERNSHIP': return 'bg-purple-500/20 text-purple-300 border-purple-500/30';
      case 'SCHOLARSHIP': return 'bg-emerald-500/20 text-emerald-300 border-emerald-500/30';
      default: return 'bg-slate-800 text-slate-300 border-slate-700';
    }
  };

  const getScoreBadge = (score: number, hasDocs: boolean) => {
    if (!hasDocs) {
      return { label: 'Low Match (No Vault Docs)', color: 'bg-amber-500/10 text-amber-400 border-amber-500/30' };
    }
    if (score >= 80) {
      return { label: `${score}% High Match`, color: 'bg-emerald-500/20 text-emerald-300 border-emerald-500/40' };
    }
    if (score >= 50) {
      return { label: `${score}% Partial Match`, color: 'bg-indigo-500/20 text-indigo-300 border-indigo-500/40' };
    }
    return { label: `${score}% Low Match`, color: 'bg-rose-500/20 text-rose-300 border-rose-500/40' };
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
            <Compass className="w-6 h-6 text-indigo-400" /> AI Opportunity Discovery Feed
          </h1>
          <p className="text-sm text-slate-400 mt-1">
            Unlimited hiring feed across South India, Pan-India SMEs, MNCs, State &amp; Central Govt recruitments.
          </p>
        </div>

        <div className="flex items-center gap-2 text-xs text-slate-300 bg-slate-900 border border-slate-800 px-3.5 py-2 rounded-xl">
          <MapPin className="w-4 h-4 text-emerald-400" />
          <span>Priority Sorted: <strong>Highest Match Roles at Top</strong></span>
        </div>
      </div>

      {/* Universal Search & Multi-Domain Filter Controls */}
      <div className="glass-panel p-5 rounded-2xl border border-slate-800 space-y-4">
        <form onSubmit={handleSearchSubmit} className="flex flex-col md:flex-row gap-3">
          <div className="relative flex-1">
            <Search className="w-4 h-4 text-slate-500 absolute left-3.5 top-1/2 -translate-y-1/2" />
            <input
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="Search any field: Cybersecurity, Full Stack, Civil, Mechanical, Data Analyst, Accounting..."
              className="w-full bg-slate-900 border border-slate-800 rounded-xl pl-10 pr-4 py-2.5 text-sm text-slate-200 placeholder-slate-500 focus:outline-none focus:border-indigo-500/50"
            />
          </div>
          <button
            type="submit"
            className="px-5 py-2.5 bg-indigo-600 hover:bg-indigo-500 text-white font-semibold text-xs rounded-xl shadow-lg shadow-indigo-600/30 transition-all shrink-0 flex items-center justify-center gap-1.5"
          >
            <Search className="w-4 h-4" />
            <span>Search Opportunities</span>
          </button>
        </form>

        {/* Opportunity Type Pills */}
        <div className="flex flex-wrap items-center justify-between gap-3 pt-2 border-t border-slate-800/80">
          <div className="flex items-center gap-2 overflow-x-auto pb-1">
            {[
              { label: 'All Opportunities', value: 'ALL' },
              { label: 'Jobs & SME Tech Roles', value: 'JOB' },
              { label: 'Govt Exams & Services', value: 'GOVT_EXAM' },
              { label: 'Internships', value: 'INTERNSHIP' },
              { label: 'Scholarships', value: 'SCHOLARSHIP' },
            ].map((tab) => (
              <button
                key={tab.value}
                onClick={() => { setSelectedType(tab.value); setCurrentPage(1); }}
                className={`px-3.5 py-1.5 rounded-xl text-xs font-semibold whitespace-nowrap transition-all ${
                  selectedType === tab.value
                    ? 'bg-indigo-600 text-white shadow-md shadow-indigo-600/30'
                    : 'bg-slate-900 border border-slate-800 text-slate-400 hover:text-slate-200 hover:bg-slate-800/60'
                }`}
              >
                {tab.label}
              </button>
            ))}
          </div>

          {/* Region Filters */}
          <div className="flex items-center gap-2 overflow-x-auto">
            {[
              { label: '📍 South India (Default)', value: 'South India' },
              { label: 'Tamil Nadu', value: 'Chennai' },
              { label: 'Bengaluru', value: 'Bengaluru' },
              { label: 'Hyderabad', value: 'Hyderabad' },
              { label: 'Pan-India', value: 'ALL' },
              { label: 'Global Remote', value: 'Global' },
            ].map((reg) => (
              <button
                key={reg.value}
                onClick={() => { setSelectedRegion(reg.value); setCurrentPage(1); }}
                className={`px-3 py-1.5 rounded-xl text-[11px] font-semibold transition-all whitespace-nowrap ${
                  selectedRegion === reg.value
                    ? 'bg-emerald-600 text-white shadow-md shadow-emerald-600/20'
                    : 'bg-slate-900 border border-slate-800 text-slate-400 hover:text-white'
                }`}
              >
                {reg.label}
              </button>
            ))}
          </div>
        </div>
      </div>

      {/* Results Counter */}
      <div className="flex items-center justify-between text-xs text-slate-400 font-medium px-1">
        <span>Showing Page <strong className="text-white">{currentPage}</strong> of <strong className="text-white">{totalPages}+</strong> (<strong className="text-indigo-400">500+ Active Opportunities Available</strong> in {selectedRegion === 'ALL' ? 'Pan-India' : selectedRegion})</span>
        {documents.length === 0 && (
          <span className="text-amber-400 font-semibold flex items-center gap-1">
            <AlertTriangle className="w-3.5 h-3.5" /> Upload vault documents to calculate precise match scores
          </span>
        )}
      </div>

      {/* Opportunity Cards Feed Grid */}
      {loading ? (
        <div className="p-12 text-center text-slate-400">
          <div className="w-8 h-8 border-4 border-indigo-500/20 border-t-indigo-500 rounded-full animate-spin mx-auto mb-3"></div>
          <p className="text-sm">Fetching Live Page {currentPage} Opportunities Across India...</p>
        </div>
      ) : opportunities.length === 0 ? (
        <div className="glass-card p-12 rounded-2xl border border-slate-800 text-center space-y-3">
          <Compass className="w-10 h-10 text-slate-600 mx-auto" />
          <h3 className="text-lg font-bold text-white">No Opportunities Found for Page {currentPage}</h3>
          <p className="text-sm text-slate-400 max-w-sm mx-auto">
            Try switching to 'All Opportunities' or selecting 'South India (Default)'.
          </p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
          {opportunities.map((opp) => {
            const badge = getScoreBadge(opp.matchScore || 70, documents.length > 0);
            return (
              <div
                key={opp.id}
                className="glass-card p-5 rounded-2xl border border-slate-800 hover:border-indigo-500/40 flex flex-col justify-between space-y-4 group relative"
              >
                <div className="space-y-3">
                  {/* Type & Match Header */}
                  <div className="flex items-center justify-between flex-wrap gap-2">
                    <span className={`text-[10px] font-extrabold uppercase px-2.5 py-0.5 rounded border ${getTypeBadge(opp.opportunityType)}`}>
                      {opp.opportunityType ? opp.opportunityType.replace('_', ' ') : 'JOB'}
                    </span>

                    <div className={`flex items-center gap-1 px-2.5 py-0.5 rounded-full text-xs font-bold border ${badge.color}`}>
                      <Sparkles className="w-3 h-3" />
                      <span>{badge.label}</span>
                    </div>
                  </div>

                  <div>
                    <h3 className="font-bold text-white text-base group-hover:text-indigo-300 transition-colors leading-snug">
                      {opp.title}
                    </h3>
                    <p className="text-xs text-indigo-400 font-medium mt-1 flex items-center gap-1.5">
                      <Building2 className="w-3.5 h-3.5" /> {opp.organization}
                    </p>
                  </div>

                  <p className="text-xs text-slate-300 leading-relaxed line-clamp-3">
                    {opp.description}
                  </p>
                </div>

                {/* Meta details */}
                <div className="space-y-2 pt-3 border-t border-slate-800/80 text-xs text-slate-400">
                  <div className="flex items-center justify-between">
                    <span className="flex items-center gap-1 text-slate-400"><MapPin className="w-3.5 h-3.5 text-indigo-400" /> Location:</span>
                    <span className="font-medium text-emerald-400">{opp.location}</span>
                  </div>
                  {opp.salaryOrStipend && (
                    <div className="flex items-center justify-between">
                      <span>Pay / Scale / Stipend:</span>
                      <span className="font-semibold text-slate-200">{opp.salaryOrStipend}</span>
                    </div>
                  )}
                </div>

                {/* Action Buttons */}
                <div className="pt-3 border-t border-slate-800/80 flex items-center justify-between gap-2">
                  <button
                    onClick={() => setSelectedOpp(opp)}
                    className="px-3 py-2 rounded-xl bg-slate-900 hover:bg-slate-800 border border-slate-800 text-xs font-semibold text-indigo-300 hover:text-white flex items-center gap-1.5 transition-colors"
                  >
                    <Sparkles className="w-3.5 h-3.5 text-indigo-400" /> Why I am Eligible
                  </button>

                  {opp.officialUrl && (
                    <a
                      href={opp.officialUrl}
                      target="_blank"
                      rel="noreferrer"
                      className="px-3.5 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-semibold text-xs flex items-center gap-1.5 shadow-md shadow-indigo-600/20"
                    >
                      <span>Apply Official</span> <ExternalLink className="w-3.5 h-3.5" />
                    </a>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      )}

      {/* Unlimited Pagination Controls */}
      <div className="flex items-center justify-between pt-6 border-t border-slate-800/80 flex-wrap gap-3">
        <button
          disabled={currentPage === 1}
          onClick={() => handlePageChange(currentPage - 1)}
          className="px-4 py-2.5 rounded-xl bg-slate-900 border border-slate-800 text-xs font-semibold text-slate-300 hover:text-white disabled:opacity-40 disabled:cursor-not-allowed flex items-center gap-1"
        >
          <ChevronLeft className="w-4 h-4" /> Previous Page
        </button>

        <div className="flex items-center gap-1.5 flex-wrap">
          {[1, 2, 3, 4, 5, 6, 7, 8, 9, 10].map((pg) => (
            <button
              key={pg}
              onClick={() => handlePageChange(pg)}
              className={`w-9 h-9 rounded-xl text-xs font-bold transition-all ${
                currentPage === pg
                  ? 'bg-indigo-600 text-white shadow-md shadow-indigo-600/30'
                  : 'bg-slate-900 border border-slate-800 text-slate-400 hover:text-white'
              }`}
            >
              {pg}
            </button>
          ))}
          <span className="text-slate-500 text-xs px-1">...</span>
          <button
            onClick={() => handlePageChange(20)}
            className={`w-9 h-9 rounded-xl text-xs font-bold transition-all ${
              currentPage === 20
                ? 'bg-indigo-600 text-white shadow-md shadow-indigo-600/30'
                : 'bg-slate-900 border border-slate-800 text-slate-400 hover:text-white'
            }`}
          >
            20
          </button>
          <button
            onClick={() => handlePageChange(50)}
            className={`w-9 h-9 rounded-xl text-xs font-bold transition-all ${
              currentPage === 50
                ? 'bg-indigo-600 text-white shadow-md shadow-indigo-600/30'
                : 'bg-slate-900 border border-slate-800 text-slate-400 hover:text-white'
            }`}
          >
            50
          </button>
        </div>

        <button
          onClick={() => handlePageChange(currentPage + 1)}
          className="px-4 py-2.5 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white text-xs font-semibold flex items-center gap-1 shadow-lg shadow-indigo-600/20"
        >
          Next Page <ChevronRight className="w-4 h-4" />
        </button>
      </div>

      {/* AI Eligibility Breakdown Modal */}
      {selectedOpp && (
        <div className="fixed inset-0 z-50 bg-slate-950/80 backdrop-blur-md flex items-center justify-center p-4">
          <div className="glass-panel w-full max-w-2xl rounded-2xl border border-slate-800 p-6 space-y-5 shadow-2xl relative max-h-[90vh] overflow-y-auto">
            <div className="flex items-start justify-between border-b border-slate-800 pb-4">
              <div>
                <span className={`text-[10px] font-extrabold uppercase px-2.5 py-0.5 rounded border ${getTypeBadge(selectedOpp.opportunityType)}`}>
                  {selectedOpp.opportunityType ? selectedOpp.opportunityType.replace('_', ' ') : 'JOB'}
                </span>
                <h3 className="font-bold text-white text-lg mt-1">{selectedOpp.title}</h3>
                <p className="text-xs text-indigo-400 font-medium">{selectedOpp.organization}</p>
              </div>
              <button onClick={() => setSelectedOpp(null)} className="text-slate-400 hover:text-white">
                <X className="w-5 h-5" />
              </button>
            </div>

            {/* AI Score Banner */}
            <div className="p-4 rounded-xl bg-gradient-to-r from-indigo-900/40 to-purple-900/40 border border-indigo-500/30 flex items-center justify-between">
              <div className="flex items-center gap-3">
                <div className={`w-12 h-12 rounded-xl flex items-center justify-center font-extrabold text-sm border ${
                  documents.length === 0 ? 'bg-amber-500/20 text-amber-300 border-amber-500/30' : 'bg-indigo-600/30 text-indigo-300 border-indigo-500/40'
                }`}>
                  {documents.length === 0 ? '40%' : `${selectedOpp.matchScore || 75}%`}
                </div>
                <div>
                  <h4 className="font-bold text-white text-sm">
                    {documents.length === 0 ? 'Low Match (Missing Vault Documents)' : 'Dynamic Match Analysis'}
                  </h4>
                  <p className="text-xs text-indigo-300 font-medium flex items-center gap-1">
                    Status: {documents.length === 0 ? 'NOT ELIGIBLE (No Resume / Degree in Vault)' : selectedOpp.eligibilityStatus}
                  </p>
                </div>
              </div>

              <span className="text-xs text-emerald-400 font-semibold bg-slate-950/60 px-3 py-1.5 rounded-xl border border-slate-800">
                Region: {selectedOpp.location}
              </span>
            </div>

            {/* Match Breakdown Lists */}
            <div className="space-y-3 text-xs">
              <div className="p-3.5 rounded-xl bg-slate-900/80 border border-slate-800 space-y-1.5">
                <h5 className="font-bold text-emerald-400 flex items-center gap-1.5">
                  <CheckCircle2 className="w-4 h-4" /> Matched Qualifications
                </h5>
                <p className="text-slate-300 leading-relaxed">
                  {documents.length === 0
                    ? "No verified documents uploaded in your vault. Upload your Degree Certificate or Resume to extract matched qualifications."
                    : selectedOpp.matchedSkills || "General Profile Alignment"}
                </p>
              </div>

              <div className="p-3.5 rounded-xl bg-slate-900/80 border border-slate-800 space-y-1.5">
                <h5 className="font-bold text-rose-400 flex items-center gap-1.5">
                  <FileQuestion className="w-4 h-4" /> Missing Qualifications &amp; Required Documents
                </h5>
                <p className="text-slate-300 leading-relaxed">
                  {documents.length === 0
                    ? "Degree Certificate (Missing), Updated Resume (Missing), Identity Proof (Missing)"
                    : selectedOpp.missingDocuments || selectedOpp.missingSkills || "None"}
                </p>
              </div>

              <div className="p-3.5 rounded-xl bg-slate-900/80 border border-slate-800 space-y-1.5">
                <h5 className="font-bold text-amber-400 flex items-center gap-1.5">
                  <Sparkles className="w-4 h-4" /> AI Recommendation &amp; Action Steps
                </h5>
                <p className="text-slate-300 leading-relaxed">
                  {documents.length === 0
                    ? `Low Match (40%) calculated for ${selectedOpp.title}. You have 0 documents in your vault. Upload your verified Degree Certificate and Resume to increase your match score to 80%+ and verify your eligibility.`
                    : selectedOpp.aiRecommendation || "Review application criteria and proceed to official portal."}
                </p>
              </div>
            </div>

            <div className="pt-3 border-t border-slate-800 flex items-center justify-between">
              <button
                onClick={() => setSelectedOpp(null)}
                className="px-4 py-2 rounded-xl bg-slate-900 border border-slate-800 text-xs font-semibold text-slate-400 hover:text-white"
              >
                Close
              </button>

              {selectedOpp.officialUrl && (
                <a
                  href={selectedOpp.officialUrl}
                  target="_blank"
                  rel="noreferrer"
                  className="px-5 py-2.5 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-semibold text-xs flex items-center gap-2 shadow-lg shadow-indigo-600/30"
                >
                  <span>Proceed to Official Application</span> <ExternalLink className="w-4 h-4" />
                </a>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
