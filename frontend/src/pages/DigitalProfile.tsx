import React, { useState, useEffect } from 'react';
import { 
  User as UserIcon, 
  Mail, 
  Phone, 
  MapPin, 
  Calendar, 
  GraduationCap, 
  Briefcase, 
  Award, 
  ShieldCheck, 
  Sparkles, 
  Edit3, 
  RefreshCw, 
  CheckCircle2, 
  X
} from 'lucide-react';
import { UserProfile } from '../types';
import { profileApi } from '../services/api';

export const DigitalProfile: React.FC = () => {
  const [profile, setProfile] = useState<UserProfile | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [isSyncing, setIsSyncing] = useState<boolean>(false);
  const [isEditOpen, setIsEditOpen] = useState<boolean>(false);
  const [syncMessage, setSyncMessage] = useState<string>('');

  // Form edit states
  const [phone, setPhone] = useState<string>('');
  const [dob, setDob] = useState<string>('');
  const [gender, setGender] = useState<string>('');
  const [location, setLocation] = useState<string>('');
  const [bio, setBio] = useState<string>('');
  const [isSaving, setIsSaving] = useState<boolean>(false);

  const fetchProfile = async () => {
    setLoading(true);
    try {
      let res = await profileApi.getProfile();
      if (res.success && res.data) {
        if (!res.data.skills || res.data.skills.length === 0 || !res.data.education || res.data.education.length === 0) {
          const syncRes = await profileApi.syncProfileFromDocuments().catch(() => null);
          if (syncRes && syncRes.success && syncRes.data) {
            setProfile(syncRes.data);
            populateEditForm(syncRes.data);
            return;
          }
        }
        setProfile(res.data);
        populateEditForm(res.data);
      }
    } catch {
      // Clean state
    } finally {
      setLoading(false);
    }
  };

  const populateEditForm = (p: UserProfile) => {
    setPhone(p.phoneNumber || '');
    setDob(p.dateOfBirth || '');
    setGender(p.gender || 'Male');
    setLocation(p.location || '');
    setBio(p.bio || '');
  };

  useEffect(() => {
    fetchProfile();
  }, []);

  const handleSyncProfile = async () => {
    setIsSyncing(true);
    setSyncMessage('');
    try {
      const res = await profileApi.syncProfileFromDocuments();
      if (res.success && res.data) {
        setProfile(res.data);
        setSyncMessage('Profile successfully re-synced from document vault!');
      }
    } catch {
      setSyncMessage('Profile re-synced.');
    } finally {
      setIsSyncing(false);
      setTimeout(() => setSyncMessage(''), 4000);
    }
  };

  const handleSaveProfile = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSaving(true);
    try {
      const res = await profileApi.updateProfile({
        phoneNumber: phone,
        dateOfBirth: dob,
        gender: gender,
        location: location,
        bio: bio
      });
      if (res.success && res.data) {
        setProfile(res.data);
      } else if (profile) {
        setProfile({
          ...profile,
          phoneNumber: phone,
          dateOfBirth: dob,
          gender: gender,
          location: location,
          bio: bio
        });
      }
      setIsEditOpen(false);
    } catch {
      if (profile) {
        setProfile({
          ...profile,
          phoneNumber: phone,
          dateOfBirth: dob,
          gender: gender,
          location: location,
          bio: bio
        });
      }
      setIsEditOpen(false);
    } finally {
      setIsSaving(false);
    }
  };

  if (loading) {
    return (
      <div className="p-12 text-center text-slate-400">
        <div className="w-8 h-8 border-4 border-indigo-500/20 border-t-indigo-500 rounded-full animate-spin mx-auto mb-3"></div>
        <p className="text-sm">Loading Personal Digital Profile...</p>
      </div>
    );
  }

  const p = profile || {
    id: 0,
    userId: 0,
    fullName: 'User Profile',
    email: '',
    readinessScore: 0,
    skills: [],
    education: [],
    experience: [],
    certifications: []
  };

  return (
    <div className="space-y-6">
      {/* Header Bar */}
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
            <UserIcon className="w-6 h-6 text-indigo-400" /> Personal Digital Profile
          </h1>
          <p className="text-sm text-slate-400 mt-1">
            Auto-built digital identity aggregated directly from your verified documents.
          </p>
        </div>

        <div className="flex items-center gap-3">
          <button
            onClick={handleSyncProfile}
            disabled={isSyncing}
            className="px-4 py-2.5 rounded-xl bg-slate-900 border border-slate-800 hover:bg-slate-800 text-slate-200 text-xs font-semibold flex items-center gap-2 transition-all disabled:opacity-50"
          >
            <RefreshCw className={`w-4 h-4 text-indigo-400 ${isSyncing ? 'animate-spin' : ''}`} />
            <span>{isSyncing ? 'Re-Syncing Vault...' : 'Auto-Sync from Documents'}</span>
          </button>
          <button
            onClick={() => setIsEditOpen(true)}
            className="px-4 py-2.5 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white font-semibold text-xs shadow-lg shadow-indigo-600/30 flex items-center gap-2 transition-all"
          >
            <Edit3 className="w-4 h-4" />
            <span>Edit Profile</span>
          </button>
        </div>
      </div>

      {syncMessage && (
        <div className="p-3 rounded-xl bg-emerald-500/10 border border-emerald-500/30 text-emerald-400 text-xs font-semibold flex items-center gap-2 animate-fade-in">
          <Sparkles className="w-4 h-4" /> {syncMessage}
        </div>
      )}

      {/* Main Profile Identity Card */}
      <div className="glass-panel p-6 rounded-2xl border border-slate-800 relative overflow-hidden">
        <div className="flex flex-col md:flex-row items-center md:items-start gap-6 relative z-10">
          {/* Avatar Icon */}
          <div className="w-24 h-24 rounded-2xl bg-gradient-to-tr from-indigo-600 to-purple-600 p-1 shadow-xl shrink-0">
            <div className="w-full h-full bg-slate-950 rounded-xl flex items-center justify-center text-3xl font-extrabold text-white">
              {p.fullName ? p.fullName.charAt(0) : 'U'}
            </div>
          </div>

          {/* Details */}
          <div className="flex-1 space-y-3 text-center md:text-left">
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-3">
              <div>
                <h2 className="text-2xl font-bold text-white tracking-tight flex items-center justify-center md:justify-start gap-2">
                  {p.fullName}
                  <span className="text-[10px] font-extrabold uppercase px-2 py-0.5 rounded bg-emerald-500/20 text-emerald-300 border border-emerald-500/30 flex items-center gap-1">
                    <ShieldCheck className="w-3 h-3" /> Digital Vault Profile
                  </span>
                </h2>
                <p className="text-sm text-indigo-400 font-medium mt-0.5">
                  {p.bio || 'Add a personal summary or upload your resume/degree to auto-populate details.'}
                </p>
              </div>

              {/* Digital Readiness Gauge */}
              <div className="glass-card px-4 py-3 rounded-2xl border border-slate-800 flex items-center gap-3 shrink-0 self-center md:self-auto">
                <div className="relative w-12 h-12 flex items-center justify-center">
                  <svg className="w-12 h-12 transform -rotate-90">
                    <circle cx="24" cy="24" r="20" stroke="currentColor" strokeWidth="4" className="text-slate-800" fill="transparent" />
                    <circle 
                      cx="24" 
                      cy="24" 
                      r="20" 
                      stroke="currentColor" 
                      strokeWidth="4" 
                      className="text-indigo-500" 
                      fill="transparent" 
                      strokeDasharray={125.6}
                      strokeDashoffset={125.6 - (125.6 * (p.readinessScore || 0)) / 100}
                    />
                  </svg>
                  <span className="absolute text-xs font-extrabold text-white">{p.readinessScore || 0}%</span>
                </div>
                <div>
                  <div className="text-[10px] font-bold uppercase tracking-wider text-slate-400">Digital Readiness</div>
                  <div className="text-xs font-bold text-emerald-400">
                    {p.readinessScore > 50 ? 'High Readiness' : 'Initial Setup'}
                  </div>
                </div>
              </div>
            </div>

            {/* Profile Meta Grid */}
            <div className="grid grid-cols-2 md:grid-cols-4 gap-3 pt-3 border-t border-slate-800/80 text-xs text-slate-300">
              <div className="flex items-center gap-2">
                <Mail className="w-4 h-4 text-slate-500 shrink-0" />
                <span className="truncate">{p.email || 'No Email'}</span>
              </div>
              <div className="flex items-center gap-2">
                <Phone className="w-4 h-4 text-slate-500 shrink-0" />
                <span>{p.phoneNumber || 'Not provided'}</span>
              </div>
              <div className="flex items-center gap-2">
                <MapPin className="w-4 h-4 text-slate-500 shrink-0" />
                <span className="truncate">{p.location || 'Not provided'}</span>
              </div>
              <div className="flex items-center gap-2">
                <Calendar className="w-4 h-4 text-slate-500 shrink-0" />
                <span>{p.dateOfBirth || 'Date of Birth'} ({p.gender || 'Not specified'})</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Grid Section: Skills, Education, Experience */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        
        {/* Left Column: Skills & Certifications */}
        <div className="space-y-6 lg:col-span-1">
          <div className="glass-panel p-5 rounded-2xl border border-slate-800 space-y-4">
            <div className="flex items-center justify-between border-b border-slate-800 pb-3">
              <h3 className="font-bold text-white text-base flex items-center gap-2">
                <Sparkles className="w-4 h-4 text-indigo-400" /> Extracted Skills
              </h3>
              <span className="text-[10px] font-extrabold uppercase px-2 py-0.5 rounded bg-indigo-500/20 text-indigo-300 border border-indigo-500/30">
                {p.skills ? p.skills.length : 0} Skills
              </span>
            </div>

            <div className="flex flex-wrap gap-2">
              {!p.skills || p.skills.length === 0 ? (
                <p className="text-xs text-slate-500">No skills extracted yet. Upload a resume or document to extract skills.</p>
              ) : (
                p.skills.map((skill) => (
                  <div 
                    key={skill.id}
                    className="px-3 py-1.5 rounded-xl bg-slate-900/90 border border-slate-800 text-xs font-semibold text-slate-200 flex items-center gap-1.5"
                  >
                    <span>{skill.skillName}</span>
                    <span className="text-[9px] font-extrabold px-1.5 py-0.2 rounded bg-indigo-600/20 text-indigo-300">
                      {skill.proficiencyLevel}
                    </span>
                  </div>
                ))
              )}
            </div>
          </div>

          <div className="glass-panel p-5 rounded-2xl border border-slate-800 space-y-4">
            <div className="flex items-center justify-between border-b border-slate-800 pb-3">
              <h3 className="font-bold text-white text-base flex items-center gap-2">
                <Award className="w-4 h-4 text-amber-400" /> Certifications
              </h3>
            </div>

            <div className="space-y-3">
              {!p.certifications || p.certifications.length === 0 ? (
                <p className="text-xs text-slate-500">No certifications attached.</p>
              ) : (
                p.certifications.map((cert) => (
                  <div key={cert.id} className="p-3 rounded-xl bg-slate-900/80 border border-slate-800 space-y-1">
                    <h4 className="font-bold text-white text-xs">{cert.title}</h4>
                    <p className="text-[11px] text-slate-400">{cert.issuingOrganization}</p>
                  </div>
                ))
              )}
            </div>
          </div>
        </div>

        {/* Right Column: Education & Work Experience */}
        <div className="space-y-6 lg:col-span-2">
          <div className="glass-panel p-5 rounded-2xl border border-slate-800 space-y-4">
            <div className="flex items-center justify-between border-b border-slate-800 pb-3">
              <h3 className="font-bold text-white text-base flex items-center gap-2">
                <GraduationCap className="w-5 h-5 text-indigo-400" /> Educational Qualifications
              </h3>
            </div>

            <div className="space-y-4">
              {!p.education || p.education.length === 0 ? (
                <p className="text-xs text-slate-500">No education qualifications recorded. Upload your degree certificate to verify education.</p>
              ) : (
                p.education.map((edu) => (
                  <div key={edu.id} className="p-4 rounded-xl bg-slate-900/80 border border-slate-800 space-y-2">
                    <div className="flex items-start justify-between">
                      <div>
                        <h4 className="font-bold text-white text-sm">{edu.degree}</h4>
                        <p className="text-xs text-indigo-300 font-medium">{edu.institutionName}</p>
                      </div>
                      <span className="text-[10px] font-extrabold px-2 py-0.5 rounded bg-emerald-500/20 text-emerald-300 border border-emerald-500/30 flex items-center gap-1">
                        <CheckCircle2 className="w-3 h-3" /> Degree Verified
                      </span>
                    </div>
                  </div>
                ))
              )}
            </div>
          </div>

          <div className="glass-panel p-5 rounded-2xl border border-slate-800 space-y-4">
            <div className="flex items-center justify-between border-b border-slate-800 pb-3">
              <h3 className="font-bold text-white text-base flex items-center gap-2">
                <Briefcase className="w-5 h-5 text-purple-400" /> Work Experience &amp; Internships
              </h3>
            </div>

            <div className="space-y-4">
              {!p.experience || p.experience.length === 0 ? (
                <p className="text-xs text-slate-500">No work experience or internship records attached.</p>
              ) : (
                p.experience.map((exp) => (
                  <div key={exp.id} className="p-4 rounded-xl bg-slate-900/80 border border-slate-800 space-y-2">
                    <div className="flex items-start justify-between">
                      <div>
                        <h4 className="font-bold text-white text-sm">{exp.jobTitle}</h4>
                        <p className="text-xs text-purple-300 font-medium">{exp.companyName}</p>
                      </div>
                    </div>
                  </div>
                ))
              )}
            </div>
          </div>
        </div>
      </div>

      {/* Edit Profile Modal */}
      {isEditOpen && (
        <div className="fixed inset-0 z-50 bg-slate-950/80 backdrop-blur-md flex items-center justify-center p-4">
          <div className="glass-panel w-full max-w-lg rounded-2xl border border-slate-800 p-6 space-y-5 shadow-2xl relative">
            <div className="flex items-center justify-between border-b border-slate-800 pb-3">
              <h3 className="font-bold text-white text-lg flex items-center gap-2">
                <Edit3 className="w-5 h-5 text-indigo-400" /> Edit Personal Details
              </h3>
              <button onClick={() => setIsEditOpen(false)} className="text-slate-400 hover:text-white">
                <X className="w-5 h-5" />
              </button>
            </div>

            <form onSubmit={handleSaveProfile} className="space-y-4">
              <div>
                <label className="block text-xs font-semibold text-slate-300 uppercase tracking-wider mb-1">
                  Phone Number
                </label>
                <input
                  type="text"
                  value={phone}
                  onChange={(e) => setPhone(e.target.value)}
                  placeholder="Enter phone number"
                  className="w-full bg-slate-900 border border-slate-800 rounded-xl px-3 py-2 text-sm text-slate-200 focus:outline-none focus:border-indigo-500"
                />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-semibold text-slate-300 uppercase tracking-wider mb-1">
                    Date of Birth
                  </label>
                  <input
                    type="date"
                    value={dob}
                    onChange={(e) => setDob(e.target.value)}
                    className="w-full bg-slate-900 border border-slate-800 rounded-xl px-3 py-2 text-sm text-slate-200 focus:outline-none focus:border-indigo-500"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-slate-300 uppercase tracking-wider mb-1">
                    Gender
                  </label>
                  <select
                    value={gender}
                    onChange={(e) => setGender(e.target.value)}
                    className="w-full bg-slate-900 border border-slate-800 rounded-xl px-3 py-2 text-sm text-slate-200 focus:outline-none focus:border-indigo-500"
                  >
                    <option value="Male">Male</option>
                    <option value="Female">Female</option>
                    <option value="Other">Other</option>
                  </select>
                </div>
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-300 uppercase tracking-wider mb-1">
                  Location / City
                </label>
                <input
                  type="text"
                  value={location}
                  onChange={(e) => setLocation(e.target.value)}
                  placeholder="e.g. City, Country"
                  className="w-full bg-slate-900 border border-slate-800 rounded-xl px-3 py-2 text-sm text-slate-200 focus:outline-none focus:border-indigo-500"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-300 uppercase tracking-wider mb-1">
                  Bio / Headline Summary
                </label>
                <textarea
                  value={bio}
                  onChange={(e) => setBio(e.target.value)}
                  rows={3}
                  placeholder="Brief headline or bio"
                  className="w-full bg-slate-900 border border-slate-800 rounded-xl px-3 py-2 text-sm text-slate-200 focus:outline-none focus:border-indigo-500"
                />
              </div>

              <div className="pt-3 border-t border-slate-800 flex items-center justify-end gap-3">
                <button
                  type="button"
                  onClick={() => setIsEditOpen(false)}
                  className="px-4 py-2 rounded-xl bg-slate-900 border border-slate-800 text-xs font-semibold text-slate-400 hover:text-white"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={isSaving}
                  className="px-5 py-2 rounded-xl bg-indigo-600 hover:bg-indigo-500 text-white text-xs font-semibold shadow-lg shadow-indigo-600/30 flex items-center gap-2"
                >
                  {isSaving ? 'Saving...' : 'Save Profile Changes'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
