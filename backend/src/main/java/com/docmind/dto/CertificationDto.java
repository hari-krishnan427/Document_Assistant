package com.docmind.dto;

import java.time.LocalDate;

public class CertificationDto {
    private Long id;
    private String title;
    private String issuingOrganization;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String credentialId;
    private Long verifiedByDocId;

    public CertificationDto() {}

    public CertificationDto(Long id, String title, String issuingOrganization, LocalDate issueDate, 
                            LocalDate expiryDate, String credentialId, Long verifiedByDocId) {
        this.id = id;
        this.title = title;
        this.issuingOrganization = issuingOrganization;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.credentialId = credentialId;
        this.verifiedByDocId = verifiedByDocId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getIssuingOrganization() { return issuingOrganization; }
    public void setIssuingOrganization(String issuingOrganization) { this.issuingOrganization = issuingOrganization; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getCredentialId() { return credentialId; }
    public void setCredentialId(String credentialId) { this.credentialId = credentialId; }

    public Long getVerifiedByDocId() { return verifiedByDocId; }
    public void setVerifiedByDocId(Long verifiedByDocId) { this.verifiedByDocId = verifiedByDocId; }

    public static CertificationDtoBuilder builder() { return new CertificationDtoBuilder(); }

    public static class CertificationDtoBuilder {
        private Long id;
        private String title;
        private String issuingOrganization;
        private LocalDate issueDate;
        private LocalDate expiryDate;
        private String credentialId;
        private Long verifiedByDocId;

        public CertificationDtoBuilder id(Long id) { this.id = id; return this; }
        public CertificationDtoBuilder title(String title) { this.title = title; return this; }
        public CertificationDtoBuilder issuingOrganization(String issuingOrganization) { this.issuingOrganization = issuingOrganization; return this; }
        public CertificationDtoBuilder issueDate(LocalDate issueDate) { this.issueDate = issueDate; return this; }
        public CertificationDtoBuilder expiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; return this; }
        public CertificationDtoBuilder credentialId(String credentialId) { this.credentialId = credentialId; return this; }
        public CertificationDtoBuilder verifiedByDocId(Long verifiedByDocId) { this.verifiedByDocId = verifiedByDocId; return this; }

        public CertificationDto build() {
            return new CertificationDto(id, title, issuingOrganization, issueDate, expiryDate, credentialId, verifiedByDocId);
        }
    }
}
