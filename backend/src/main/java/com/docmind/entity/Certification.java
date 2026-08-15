package com.docmind.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_certifications")
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "issuing_organization")
    private String issuingOrganization;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "credential_id")
    private String credentialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by_doc_id")
    private DocumentEntity verifiedByDoc;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Certification() {}

    public Certification(Long id, User user, String title, String issuingOrganization, LocalDate issueDate, 
                         LocalDate expiryDate, String credentialId, DocumentEntity verifiedByDoc, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.issuingOrganization = issuingOrganization;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.credentialId = credentialId;
        this.verifiedByDoc = verifiedByDoc;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

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

    public DocumentEntity getVerifiedByDoc() { return verifiedByDoc; }
    public void setVerifiedByDoc(DocumentEntity verifiedByDoc) { this.verifiedByDoc = verifiedByDoc; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static CertificationBuilder builder() { return new CertificationBuilder(); }

    public static class CertificationBuilder {
        private Long id;
        private User user;
        private String title;
        private String issuingOrganization;
        private LocalDate issueDate;
        private LocalDate expiryDate;
        private String credentialId;
        private DocumentEntity verifiedByDoc;
        private LocalDateTime createdAt;

        public CertificationBuilder id(Long id) { this.id = id; return this; }
        public CertificationBuilder user(User user) { this.user = user; return this; }
        public CertificationBuilder title(String title) { this.title = title; return this; }
        public CertificationBuilder issuingOrganization(String issuingOrganization) { this.issuingOrganization = issuingOrganization; return this; }
        public CertificationBuilder issueDate(LocalDate issueDate) { this.issueDate = issueDate; return this; }
        public CertificationBuilder expiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; return this; }
        public CertificationBuilder credentialId(String credentialId) { this.credentialId = credentialId; return this; }
        public CertificationBuilder verifiedByDoc(DocumentEntity verifiedByDoc) { this.verifiedByDoc = verifiedByDoc; return this; }
        public CertificationBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Certification build() {
            return new Certification(id, user, title, issuingOrganization, issueDate, expiryDate, credentialId, verifiedByDoc, createdAt);
        }
    }
}
