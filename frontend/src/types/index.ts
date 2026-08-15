export interface User {
  id: number;
  email: string;
  fullName: string;
  role: string;
  createdAt: string;
}

export interface AuthResponse {
  token: string;
  tokenType: string;
  user: User;
}

export interface SkillItem {
  id: number;
  skillName: string;
  proficiencyLevel: string;
  verifiedByDocId?: number;
  verifiedByDocName?: string;
}

export interface EducationItem {
  id: number;
  institutionName: string;
  degree: string;
  fieldOfStudy?: string;
  startYear?: number;
  endYear?: number;
  gradeOrCgpa?: string;
  verifiedByDocId?: number;
}

export interface ExperienceItem {
  id: number;
  companyName: string;
  jobTitle: string;
  location?: string;
  startDate?: string;
  endDate?: string;
  isCurrent?: boolean;
  description?: string;
  verifiedByDocId?: number;
}

export interface CertificationItem {
  id: number;
  title: string;
  issuingOrganization?: string;
  issueDate?: string;
  expiryDate?: string;
  credentialId?: string;
  verifiedByDocId?: number;
}

export interface UserProfile {
  id: number;
  userId: number;
  fullName: string;
  email: string;
  phoneNumber?: string;
  dateOfBirth?: string;
  gender?: string;
  location?: string;
  bio?: string;
  readinessScore: number;
  skills: SkillItem[];
  education: EducationItem[];
  experience: ExperienceItem[];
  certifications: CertificationItem[];
}

export interface DocumentItem {
  id: number;
  fileName: string;
  filePath: string;
  fileType: string;
  fileSize: number;
  category: string;
  documentType?: string;
  issueDate?: string;
  expiryDate?: string;
  status: 'ACTIVE' | 'EXPIRING_SOON' | 'EXPIRED';
  isEncrypted: boolean;
  createdAt: string;
}

export interface OpportunityItem {
  id: number;
  title: string;
  organization: string;
  opportunityType: 'JOB' | 'GOVT_EXAM' | 'INTERNSHIP' | 'SCHOLARSHIP' | 'COMPETITION';
  description: string;
  location: string;
  salaryOrStipend?: string;
  deadline: string;
  officialUrl?: string;
  source: string;
  publishedDate: string;
  matchScore?: number;
  eligibilityStatus?: 'ELIGIBLE' | 'PARTIALLY_ELIGIBLE' | 'NOT_ELIGIBLE';
  matchedSkills?: string;
  missingSkills?: string;
  missingDocuments?: string;
  aiRecommendation?: string;
}

export interface ReminderItem {
  id: number;
  userId: number;
  documentId?: number;
  documentName?: string;
  title: string;
  message: string;
  reminderDate: string;
  priority: 'LOW' | 'MEDIUM' | 'HIGH' | 'URGENT';
  isRead: boolean;
  createdAt: string;
}

export interface DocumentBundleItem {
  id: number;
  userId: number;
  opportunityId?: number;
  opportunityTitle?: string;
  bundleName: string;
  bundlePath: string;
  fileCount: number;
  createdAt: string;
}

export interface AuditLogItem {
  id: number;
  userId: number;
  userName: string;
  actionType: string;
  resource: string;
  details: string;
  ipAddress: string;
  createdAt: string;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}
