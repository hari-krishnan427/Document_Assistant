import re
from typing import Dict, List, Any

class OpportunityMatcher:
    """
    Universal AI Opportunity & Candidate Vault Document Matcher Engine.
    Calculates dynamic match scores, matched skills, missing skills, missing documents,
    and actionable eligibility recommendations across all fields (Tech, Non-Tech, Govt Exams, Internships).
    """

    def __init__(self):
        self.skill_dictionary = [
            "python", "java", "sql", "spring boot", "react", "typescript", "postgresql",
            "cybersecurity", "rest apis", "aws", "docker", "machine learning", "ai",
            "c++", "data analysis", "cloud", "networking", "linux", "git", "devops",
            "excel", "finance", "accounting", "communication", "project management",
            "cad", "matlab", "embedded", "circuits", "electrical", "civil engineering"
        ]

    def match(self, resume_text: str, job_title: str, job_description: str, required_skills: List[str]) -> Dict[str, Any]:
        has_vault_docs = bool(resume_text and len(resume_text.strip()) > 30)
        combined_text = (resume_text or "").lower()
        job_text = (job_description or "").lower() + " " + (job_title or "").lower()

        matched_skills = []
        missing_skills = []

        # Compare skills extracted from vault documents vs job criteria
        for skill in self.skill_dictionary:
            if skill in job_text:
                if has_vault_docs and skill in combined_text:
                    matched_skills.append(skill.title())
                else:
                    missing_skills.append(skill.title())

        # Additional required skills passed explicitly
        for s in required_skills:
            s_clean = s.strip().title()
            if has_vault_docs and s.lower() in combined_text:
                if s_clean not in matched_skills:
                    matched_skills.append(s_clean)
            else:
                if s_clean not in missing_skills and s_clean not in matched_skills:
                    missing_skills.append(s_clean)

        # Dynamic match percentage calculation
        if not has_vault_docs:
            match_percentage = 40  # Low match due to missing vault documents
            eligibility = "NOT_ELIGIBLE"
            matched_str = "No verified documents uploaded in vault"
            missing_str = ", ".join(missing_skills[:5]) if missing_skills else "Degree Certificate / Resume required"
            missing_docs = "Degree Certificate (Missing), Updated Resume (Missing), Identity Proof (Missing)"
            ai_rec = (
                f"Low Match (40%) for {job_title}. You have not uploaded any verified documents to your vault. "
                "Upload your Degree Certificate and Resume to extract your qualifications and unlock full match eligibility."
            )
        else:
            total_reqs = len(matched_skills) + len(missing_skills)
            if total_reqs > 0:
                match_percentage = int((len(matched_skills) / total_reqs) * 100)
            else:
                match_percentage = 75

            # Degree verification check
            has_degree = any(deg in combined_text for deg in ["b.e", "b.tech", "degree", "bachelor", "master", "m.tech", "bsc", "msc", "diploma"])
            if has_degree:
                match_percentage = min(98, match_percentage + 15)

            if match_percentage >= 80:
                eligibility = "ELIGIBLE"
            elif match_percentage >= 50:
                eligibility = "PARTIALLY_ELIGIBLE"
            else:
                eligibility = "NOT_ELIGIBLE"

            matched_str = ", ".join(matched_skills) if matched_skills else "Basic Profile Alignment"
            missing_str = ", ".join(missing_skills[:5]) if missing_skills else "None"
            missing_docs = "Verified in Vault" if has_degree else "Degree Certificate verification recommended"

            ai_rec = (
                f"Calculated Match Score: {match_percentage}% ({eligibility.replace('_', ' ')}) for {job_title}. "
                f"Matched skills: {matched_str}. "
                + (f"Missing criteria: {missing_str}. Upload updated certificates to raise your match score." if missing_skills else "Your qualifications strongly fulfill application criteria.")
            )

        return {
            "match_score": match_percentage,
            "eligibility_status": eligibility,
            "matched_skills": matched_str,
            "missing_skills": missing_str,
            "missing_documents": missing_docs,
            "ai_recommendation": ai_rec
        }

opportunity_matcher = OpportunityMatcher()
