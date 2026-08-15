package com.docmind.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "document_bundles")
public class DocumentBundle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opportunity_id")
    private Opportunity opportunity;

    @Column(name = "bundle_name", nullable = false)
    private String bundleName;

    @Column(name = "bundle_path", nullable = false)
    private String bundlePath;

    @Column(name = "file_count")
    private Integer fileCount = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public DocumentBundle() {}

    public DocumentBundle(Long id, User user, Opportunity opportunity, String bundleName, String bundlePath, Integer fileCount, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.opportunity = opportunity;
        this.bundleName = bundleName;
        this.bundlePath = bundlePath;
        this.fileCount = fileCount != null ? fileCount : 0;
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

    public Opportunity getOpportunity() { return opportunity; }
    public void setOpportunity(Opportunity opportunity) { this.opportunity = opportunity; }

    public String getBundleName() { return bundleName; }
    public void setBundleName(String bundleName) { this.bundleName = bundleName; }

    public String getBundlePath() { return bundlePath; }
    public void setBundlePath(String bundlePath) { this.bundlePath = bundlePath; }

    public Integer getFileCount() { return fileCount; }
    public void setFileCount(Integer fileCount) { this.fileCount = fileCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static DocumentBundleBuilder builder() { return new DocumentBundleBuilder(); }

    public static class DocumentBundleBuilder {
        private Long id;
        private User user;
        private Opportunity opportunity;
        private String bundleName;
        private String bundlePath;
        private Integer fileCount = 0;
        private LocalDateTime createdAt;

        public DocumentBundleBuilder id(Long id) { this.id = id; return this; }
        public DocumentBundleBuilder user(User user) { this.user = user; return this; }
        public DocumentBundleBuilder opportunity(Opportunity opportunity) { this.opportunity = opportunity; return this; }
        public DocumentBundleBuilder bundleName(String bundleName) { this.bundleName = bundleName; return this; }
        public DocumentBundleBuilder bundlePath(String bundlePath) { this.bundlePath = bundlePath; return this; }
        public DocumentBundleBuilder fileCount(Integer fileCount) { this.fileCount = fileCount; return this; }
        public DocumentBundleBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public DocumentBundle build() {
            return new DocumentBundle(id, user, opportunity, bundleName, bundlePath, fileCount, createdAt);
        }
    }
}
