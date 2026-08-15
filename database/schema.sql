-- DocMind AI Database Schema (PostgreSQL)

-- Users & Authentication
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'ROLE_USER',
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- User Digital Profile
CREATE TABLE IF NOT EXISTS user_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    phone_number VARCHAR(20),
    date_of_birth DATE,
    gender VARCHAR(20),
    location VARCHAR(255),
    bio TEXT,
    readiness_score INT DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Document Management
CREATE TABLE IF NOT EXISTS documents (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(512) NOT NULL,
    file_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL,
    category VARCHAR(100) NOT NULL, -- Identity, Education, Employment, Resume, Government, Financial, etc.
    document_type VARCHAR(100),    -- Aadhaar, PAN, Passport, 10th Marksheet, Degree, Resume, etc.
    issue_date DATE,
    expiry_date DATE,
    status VARCHAR(50) DEFAULT 'ACTIVE', -- ACTIVE, EXPIRING_SOON, EXPIRED
    is_encrypted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Extracted Information from OCR & AI
CREATE TABLE IF NOT EXISTS extracted_information (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT NOT NULL REFERENCES documents(id) ON DELETE CASCADE,
    field_key VARCHAR(100) NOT NULL,
    field_value TEXT NOT NULL,
    confidence_score FLOAT DEFAULT 1.0,
    is_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- User Skills
CREATE TABLE IF NOT EXISTS user_skills (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    skill_name VARCHAR(100) NOT NULL,
    proficiency_level VARCHAR(50), -- BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
    verified_by_doc_id BIGINT REFERENCES documents(id) ON DELETE SET NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- User Education
CREATE TABLE IF NOT EXISTS user_education (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    institution_name VARCHAR(255) NOT NULL,
    degree VARCHAR(100) NOT NULL,
    field_of_study VARCHAR(100),
    start_year INT,
    end_year INT,
    grade_or_cgpa VARCHAR(50),
    verified_by_doc_id BIGINT REFERENCES documents(id) ON DELETE SET NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- User Experience
CREATE TABLE IF NOT EXISTS user_experience (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    company_name VARCHAR(255) NOT NULL,
    job_title VARCHAR(100) NOT NULL,
    location VARCHAR(100),
    start_date DATE,
    end_date DATE,
    is_current BOOLEAN DEFAULT FALSE,
    description TEXT,
    verified_by_doc_id BIGINT REFERENCES documents(id) ON DELETE SET NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- User Certifications
CREATE TABLE IF NOT EXISTS user_certifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    issuing_organization VARCHAR(255) NOT NULL,
    issue_date DATE,
    expiry_date DATE,
    credential_id VARCHAR(100),
    verified_by_doc_id BIGINT REFERENCES documents(id) ON DELETE SET NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Opportunities (Jobs, Government Exams, Internships, Scholarships)
CREATE TABLE IF NOT EXISTS opportunities (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    organization VARCHAR(255) NOT NULL,
    opportunity_type VARCHAR(50) NOT NULL, -- JOB, GOVT_EXAM, INTERNSHIP, SCHOLARSHIP, COMPETITION
    description TEXT,
    location VARCHAR(100),
    salary_or_stipend VARCHAR(100),
    deadline DATE,
    official_url VARCHAR(512),
    source VARCHAR(100),
    published_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Opportunity Requirements
CREATE TABLE IF NOT EXISTS opportunity_requirements (
    id BIGSERIAL PRIMARY KEY,
    opportunity_id BIGINT NOT NULL REFERENCES opportunities(id) ON DELETE CASCADE,
    requirement_type VARCHAR(50) NOT NULL, -- SKILL, DEGREE, EXPERIENCE, AGE, DOCUMENT
    requirement_value VARCHAR(255) NOT NULL,
    is_mandatory BOOLEAN DEFAULT TRUE
);

-- Opportunity Matches & Analysis
CREATE TABLE IF NOT EXISTS opportunity_matches (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    opportunity_id BIGINT NOT NULL REFERENCES opportunities(id) ON DELETE CASCADE,
    match_score INT NOT NULL, -- 0 to 100
    eligibility_status VARCHAR(50) NOT NULL, -- ELIGIBLE, PARTIALLY_ELIGIBLE, NOT_ELIGIBLE
    matched_skills TEXT,
    missing_skills TEXT,
    missing_documents TEXT,
    ai_recommendation TEXT,
    analyzed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_user_opportunity UNIQUE (user_id, opportunity_id)
);

-- Reminders & Expiry Tracking
CREATE TABLE IF NOT EXISTS reminders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    document_id BIGINT REFERENCES documents(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    message TEXT,
    reminder_date DATE NOT NULL,
    priority VARCHAR(20) DEFAULT 'MEDIUM', -- LOW, MEDIUM, HIGH, URGENT
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Document Bundles
CREATE TABLE IF NOT EXISTS document_bundles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    opportunity_id BIGINT REFERENCES opportunities(id) ON DELETE SET NULL,
    bundle_name VARCHAR(255) NOT NULL,
    bundle_path VARCHAR(512) NOT NULL,
    file_count INT DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Security Audit Logs
CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    action_type VARCHAR(100) NOT NULL, -- LOGIN, UPLOAD, VIEW, DOWNLOAD, DELETE, BUNDLE_GENERATE
    resource VARCHAR(255),
    details TEXT,
    ip_address VARCHAR(50),
    user_agent VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
