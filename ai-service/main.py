from fastapi import FastAPI, UploadFile, File, Form, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import Optional, List, Dict, Any
import os

from services.document_processor import doc_processor
from services.opportunity_matcher import opportunity_matcher
from services.assistant_engine import assistant_engine
from services.job_search_service import job_search_service

app = FastAPI(
    title="DocMind AI Python Microservice",
    description="OCR Text Extraction, Conversational AI Assistant & Job Matcher Engine",
    version="1.0.0"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

class HealthResponse(BaseModel):
    status: str
    service: str
    version: str

class IntentRequest(BaseModel):
    query: str

class IntentResponse(BaseModel):
    intent: str
    confidence: float
    entities: Dict[str, Any]
    action_prompt: str

class ChatRequest(BaseModel):
    query: str
    user_name: Optional[str] = "Hari"
    context: Optional[Dict[str, Any]] = None

class ChatResponse(BaseModel):
    response: str
    intent: str
    action_type: str
    action_data: Dict[str, Any]
    suggested_prompts: List[str]

class MatchOpportunityRequest(BaseModel):
    resume_text: Optional[str] = ""
    job_title: str
    job_description: str
    required_skills: Optional[List[str]] = []

class MatchOpportunityResponse(BaseModel):
    match_score: int
    eligibility_status: str
    matched_skills: str
    missing_skills: str
    missing_documents: str
    ai_recommendation: str

class ProcessDocumentResponse(BaseModel):
    file_name: str
    category: str
    document_type: str
    raw_text_snippet: str
    extracted_fields: List[Dict[str, Any]]

@app.get("/health", response_model=HealthResponse)
def health_check():
    return HealthResponse(
        status="HEALTHY",
        service="DocMind AI Python Microservice",
        version="1.0.0"
    )

@app.post("/api/ai/process-document", response_model=ProcessDocumentResponse)
async def process_document(
    file: UploadFile = File(...),
    filename: Optional[str] = Form(None)
):
    try:
        content = await file.read()
        target_filename = filename or file.filename or "document.pdf"
        result = doc_processor.process_document(content, target_filename)
        return ProcessDocumentResponse(**result)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Document processing failed: {str(e)}")

@app.post("/api/ai/match-opportunity", response_model=MatchOpportunityResponse)
async def match_opportunity(req: MatchOpportunityRequest):
    try:
        result = opportunity_matcher.match(
            resume_text=req.resume_text or "",
            job_title=req.job_title,
            job_description=req.job_description,
            required_skills=req.required_skills or []
        )
        return MatchOpportunityResponse(**result)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Opportunity matching failed: {str(e)}")

@app.post("/api/ai/chat", response_model=ChatResponse)
async def chat_assistant(req: ChatRequest):
    try:
        result = assistant_engine.process_chat(
            user_query=req.query,
            user_name=req.user_name or "Hari",
            context=req.context
        )
        return ChatResponse(**result)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Chat assistant error: {str(e)}")

@app.post("/api/ai/intent", response_model=IntentResponse)
async def detect_intent(req: IntentRequest):
    query = req.query.lower().strip()
    
    intent = "GENERAL_QUERY"
    confidence = 0.85
    entities = {}
    action_prompt = "How can I assist you with your documents or opportunities today?"

    if "aadhaar" in query or "degree" in query or "passport" in query or "document" in query or "find" in query or "show" in query:
        intent = "DOCUMENT_SEARCH"
        confidence = 0.95
        if "aadhaar" in query:
            entities["document_type"] = "Aadhaar"
        elif "degree" in query:
            entities["document_type"] = "Degree Certificate"
        elif "passport" in query:
            entities["document_type"] = "Passport"
        action_prompt = "Searching your secure document vault..."

    elif "expire" in query or "expiring" in query or "validity" in query or "deadline" in query:
        intent = "EXPIRY_CHECK"
        confidence = 0.92
        action_prompt = "Checking document expiry dates and upcoming deadlines..."

    elif "job" in query or "internship" in query or "exam" in query or "match" in query or "eligible" in query or "opportunity" in query:
        intent = "ELIGIBILITY_CHECK"
        confidence = 0.94
        action_prompt = "Analyzing your digital profile against active career opportunities..."

    elif "prepare" in query or "bundle" in query or "package" in query or "apply" in query:
        intent = "DOCUMENT_BUNDLE"
        confidence = 0.96
        action_prompt = "Mapping required documents and compiling your application package..."

    return IntentResponse(
        intent=intent,
        confidence=confidence,
        entities=entities,
        action_prompt=action_prompt
    )

@app.get("/api/ai/live-jobs")
async def get_live_jobs(
    query: Optional[str] = None,
    location: Optional[str] = None,
    skills: Optional[str] = None,
    type: Optional[str] = None
):
    try:
        skill_list = skills.split(",") if skills else []
        jobs = job_search_service.fetch_live_jobs(query, location, skill_list, type)
        return {"status": "SUCCESS", "count": len(jobs), "jobs": jobs}
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Live job fetch failed: {str(e)}")

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
