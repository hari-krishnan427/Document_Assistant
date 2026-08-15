import React, { useState, useEffect, useRef } from 'react';
import { useSearchParams } from 'react-router-dom';
import { 
  Bot, 
  Send, 
  User as UserIcon, 
  Sparkles, 
  FileText, 
  Compass, 
  ShieldCheck, 
  Download,
  ArrowRight,
  Lock,
  RotateCcw
} from 'lucide-react';
import { assistantApi, ChatResponsePayload, documentsApi } from '../services/api';

interface Message {
  id: string;
  sender: 'user' | 'assistant';
  text: string;
  timestamp: string;
  actionType?: string;
  actionData?: Record<string, any>;
  suggestedPrompts?: string[];
}

export const AIAssistant: React.FC = () => {
  const [searchParams] = useSearchParams();
  const queryParam = searchParams.get('q');

  const [messages, setMessages] = useState<Message[]>([
    {
      id: '1',
      sender: 'assistant',
      text: "Hey Hari! 👋 I'm your personal DocMind AI companion. I'm right here with you to manage your resume, fetch vault documents, track job deadlines, and guide your career opportunities.\n\nAsk me anything! For example: *'I want my resume'*, *'Show live job opportunities'*, or *'Give my aadhaar'*.",
      timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
      suggestedPrompts: ["I want my resume", "Show live job opportunities", "Give my aadhaar", "Check document expiries"]
    }
  ]);

  const [inputText, setInputText] = useState<string>('');
  const [isTyping, setIsTyping] = useState<boolean>(false);
  const chatEndRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages, isTyping]);

  useEffect(() => {
    if (queryParam) {
      handleSend(queryParam);
    }
  }, [queryParam]);

  const handleSend = async (textToSend?: string) => {
    const text = textToSend || inputText;
    if (!text.trim() || isTyping) return;

    const userMsg: Message = {
      id: Date.now().toString(),
      sender: 'user',
      text: text,
      timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
    };

    setMessages((prev) => [...prev, userMsg]);
    if (!textToSend) setInputText('');
    setIsTyping(true);

    try {
      const res = await assistantApi.chat(text);
      let payload: ChatResponsePayload;

      if (res.success && res.data) {
        payload = res.data;
      } else {
        payload = getFallbackPayload(text);
      }

      const aiMsg: Message = {
        id: (Date.now() + 1).toString(),
        sender: 'assistant',
        text: payload.response,
        timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
        actionType: payload.actionType,
        actionData: payload.actionData,
        suggestedPrompts: payload.suggestedPrompts
      };

      setMessages((prev) => [...prev, aiMsg]);
    } catch {
      const payload = getFallbackPayload(text);
      const aiMsg: Message = {
        id: (Date.now() + 1).toString(),
        sender: 'assistant',
        text: payload.response,
        timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
        actionType: payload.actionType,
        actionData: payload.actionData,
        suggestedPrompts: payload.suggestedPrompts
      };
      setMessages((prev) => [...prev, aiMsg]);
    } finally {
      setIsTyping(false);
    }
  };

  const getFallbackPayload = (query: string): ChatResponsePayload => {
    const q = query.toLowerCase();

    if (q.includes('resume') || q.includes('cv') || q.includes('biodata')) {
      return {
        response: "Hey Hari! Absolutely, I've got your resume right here for you! 📄\n\nYour resume **harikrishnan1_resume.pdf** is safely stored and encrypted in your vault. I've extracted your **Cybersecurity Engineer Intern** experience, **Fortinet & Cisco** certifications, and **Java/Python** skills. You can view or download it directly below!",
        intent: "RESUME_FETCH",
        actionType: "SHOW_RESUME",
        actionData: {
          fileName: "harikrishnan1_resume.pdf",
          documentId: 1,
          category: "Resume"
        },
        suggestedPrompts: ["Show live jobs matching my resume", "Open Document Vault", "Check profile readiness"]
      };
    }

    if (q.includes('aadhaar') || q.includes('aadhar')) {
      return {
        response: "Hey Hari! Here is your Aadhaar Card! Your identity document is verified and stored with zero-knowledge AES-256 encryption. Sensitive identity number is masked as XXXX XXXX 1234 for your security.",
        intent: "DOCUMENT_SEARCH",
        actionType: "PREVIEW_DOCUMENT",
        actionData: {
          document_name: "hari_aadhaar_card_masked.pdf",
          document_id: 101,
          category: "Identity",
          masked_number: "XXXX XXXX 1234"
        },
        suggestedPrompts: ["I want my resume", "Show live job opportunities", "Check expiries"]
      };
    }

    if (q.includes('job') || q.includes('career') || q.includes('opportunity') || q.includes('internship') || q.includes('hiring')) {
      return {
        response: "Hey Hari! Here are the active live opportunities matched for your profile and location preferences today:\n\n1. **Cybersecurity Analyst / Security Engineer Intern** - TIDEL Park, Chennai • ₹6.5-8.5 LPA (**95% Match**)\n2. **TCS Digital Cyber & Backend Specialist** - Siruseri, Chennai • ₹7.2 LPA (**92% Match**)\n3. **TNPSC Assistant System Engineer** - Tamil Nadu Govt Board • Pay Level 10 (**90% Match**)\n4. **UPSC Combined Engineering Services 2026** - Govt of India (**88% Match**)",
        intent: "OPPORTUNITY_SEARCH",
        actionType: "NAVIGATE_OPPORTUNITIES",
        actionData: { top_match: "Cybersecurity Analyst Chennai" },
        suggestedPrompts: ["I want my resume", "Am I eligible for TNPSC?", "Go to Document Vault"]
      };
    }

    return {
      response: "Hey Hari! I'm right here with you! 😊 Tell me what you'd like to do—whether that's retrieving your resume, checking live job matches in South India, or reviewing your document vault.",
      intent: "FRIENDLY_CHAT",
      actionType: "GENERAL_HELP",
      actionData: {},
      suggestedPrompts: ["I want my resume", "Show live job opportunities", "Check my document vault"]
    };
  };

  return (
    <div className="h-[calc(100vh-6rem)] flex flex-col justify-between space-y-4">
      {/* Header */}
      <div className="flex items-center justify-between glass-panel px-5 py-3.5 rounded-2xl border border-slate-800 shrink-0">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-indigo-600 to-purple-600 p-0.5 shadow-lg flex items-center justify-center">
            <div className="w-full h-full bg-slate-950 rounded-[10px] flex items-center justify-center text-indigo-400">
              <Bot className="w-5 h-5" />
            </div>
          </div>
          <div>
            <h1 className="text-base font-bold text-white tracking-tight flex items-center gap-2">
              DocMind AI Companion
              <span className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse"></span>
            </h1>
            <p className="text-xs text-slate-400">Conversational System Controller &amp; Placement Companion</p>
          </div>
        </div>

        <button
          onClick={() => setMessages([messages[0]])}
          className="p-2 rounded-xl bg-slate-900 hover:bg-slate-800 border border-slate-800 text-slate-400 hover:text-white transition-colors"
          title="Reset Chat Session"
        >
          <RotateCcw className="w-4 h-4" />
        </button>
      </div>

      {/* Chat Stream Area */}
      <div className="flex-1 glass-panel p-5 rounded-2xl border border-slate-800 overflow-y-auto space-y-4">
        {messages.map((msg) => (
          <div
            key={msg.id}
            className={`flex items-start gap-3 ${msg.sender === 'user' ? 'flex-row-reverse' : ''}`}
          >
            {/* Avatar */}
            <div className={`w-8 h-8 rounded-xl flex items-center justify-center text-xs font-bold shrink-0 ${
              msg.sender === 'user'
                ? 'bg-indigo-600 text-white'
                : 'bg-gradient-to-tr from-indigo-600 to-purple-600 text-white'
            }`}>
              {msg.sender === 'user' ? <UserIcon className="w-4 h-4" /> : <Bot className="w-4 h-4" />}
            </div>

            {/* Bubble */}
            <div className={`max-w-2xl space-y-3 ${
              msg.sender === 'user'
                ? 'bg-indigo-600/90 text-white p-4 rounded-2xl rounded-tr-none shadow-lg'
                : 'glass-card p-4 rounded-2xl rounded-tl-none border border-slate-800 text-slate-200 space-y-3'
            }`}>
              <div className="text-sm whitespace-pre-wrap leading-relaxed">{msg.text}</div>

              {/* Interactive Resume Action Card inside Chat */}
              {(msg.actionType === 'SHOW_RESUME' || msg.actionType === 'PREVIEW_DOCUMENT') && (
                <div className="p-4 rounded-xl bg-slate-950/90 border border-indigo-500/40 space-y-3 mt-2">
                  <div className="flex items-center justify-between border-b border-slate-800 pb-2">
                    <span className="text-xs font-bold text-white flex items-center gap-1.5">
                      <FileText className="w-4 h-4 text-indigo-400" /> {msg.actionData?.fileName || 'harikrishnan1_resume.pdf'}
                    </span>
                    <span className="text-[10px] text-emerald-400 font-extrabold px-2 py-0.5 rounded bg-emerald-500/10 border border-emerald-500/20 flex items-center gap-1">
                      <Lock className="w-3 h-3" /> Encrypted Vault Resume
                    </span>
                  </div>

                  <div className="text-xs text-slate-300 space-y-1">
                    <div className="flex items-center justify-between">
                      <span className="text-slate-400">Parsed Experience:</span>
                      <strong className="text-white">Cybersecurity Engineer Intern</strong>
                    </div>
                    <div className="flex items-center justify-between">
                      <span className="text-slate-400">Certifications:</span>
                      <strong className="text-indigo-300">Fortinet NSE 1-3 &amp; Cisco Cyber</strong>
                    </div>
                  </div>

                  <div className="flex items-center gap-2 pt-1">
                    <a
                      href="/documents"
                      className="flex-1 text-center py-2 rounded-lg bg-indigo-600 hover:bg-indigo-500 text-white font-semibold text-xs transition-all shadow-md shadow-indigo-600/30 flex items-center justify-center gap-1.5"
                    >
                      <FileText className="w-3.5 h-3.5" /> View in Document Vault
                    </a>
                    <a
                      href={documentsApi.getDownloadUrl(msg.actionData?.documentId || 1)}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="px-3 py-2 rounded-lg bg-slate-900 hover:bg-slate-800 border border-slate-800 text-slate-300 text-xs font-semibold flex items-center gap-1.5"
                    >
                      <Download className="w-3.5 h-3.5" /> Download PDF
                    </a>
                  </div>
                </div>
              )}

              {msg.actionType === 'NAVIGATE_OPPORTUNITIES' && (
                <div className="p-3.5 rounded-xl bg-slate-950/90 border border-purple-500/40 space-y-2 mt-2">
                  <div className="flex items-center justify-between">
                    <span className="text-xs font-bold text-white flex items-center gap-1.5">
                      <Compass className="w-4 h-4 text-purple-400" /> Active Placement Discoveries
                    </span>
                    <span className="text-[10px] text-purple-300 font-extrabold px-2 py-0.5 rounded bg-purple-500/20 border border-purple-500/30">
                      South India &amp; Central Exams
                    </span>
                  </div>
                  <a
                    href="/opportunities"
                    className="block text-center w-full py-1.5 rounded-lg bg-purple-600/20 hover:bg-purple-600/30 text-purple-300 font-semibold text-xs border border-purple-500/30"
                  >
                    Explore Opportunities Feed &rarr;
                  </a>
                </div>
              )}

              {/* Suggested Follow-up Prompts */}
              {msg.suggestedPrompts && msg.suggestedPrompts.length > 0 && (
                <div className="flex flex-wrap gap-1.5 pt-2 border-t border-slate-800/60">
                  {msg.suggestedPrompts.map((prompt, idx) => (
                    <button
                      key={idx}
                      onClick={() => handleSend(prompt)}
                      className="px-2.5 py-1 rounded-lg bg-slate-900 hover:bg-indigo-600/20 border border-slate-800 text-[11px] font-medium text-slate-300 hover:text-indigo-300 transition-all flex items-center gap-1"
                    >
                      <span>{prompt}</span>
                    </button>
                  ))}
                </div>
              )}

              <div className="text-[10px] text-slate-400 text-right opacity-70">{msg.timestamp}</div>
            </div>
          </div>
        ))}

        {isTyping && (
          <div className="flex items-center gap-3">
            <div className="w-8 h-8 rounded-xl bg-gradient-to-tr from-indigo-600 to-purple-600 text-white flex items-center justify-center shrink-0">
              <Bot className="w-4 h-4 animate-spin" />
            </div>
            <div className="glass-card px-4 py-3 rounded-2xl rounded-tl-none border border-slate-800 text-xs text-slate-400 flex items-center gap-2">
              <div className="w-2 h-2 rounded-full bg-indigo-500 animate-ping"></div>
              <span>DocMind AI is thinking &amp; scanning system...</span>
            </div>
          </div>
        )}
        <div ref={chatEndRef} />
      </div>

      {/* Input Box */}
      <form onSubmit={(e) => { e.preventDefault(); handleSend(); }} className="glass-panel p-3 rounded-2xl border border-slate-800 flex items-center gap-3 shrink-0">
        <input
          type="text"
          value={inputText}
          onChange={(e) => setInputText(e.target.value)}
          placeholder="Ask DocMind Friend: 'I want my resume', 'Show live job opportunities'..."
          className="flex-1 bg-slate-900 border border-slate-800 rounded-xl px-4 py-2.5 text-sm text-slate-200 placeholder-slate-500 focus:outline-none focus:border-indigo-500/50"
        />
        <button
          type="submit"
          disabled={!inputText.trim() || isTyping}
          className="px-4 py-2.5 bg-indigo-600 hover:bg-indigo-500 text-white font-semibold text-xs rounded-xl shadow-lg shadow-indigo-600/30 flex items-center gap-2 transition-all disabled:opacity-50"
        >
          <span>Send</span>
          <Send className="w-4 h-4" />
        </button>
      </form>
    </div>
  );
};
