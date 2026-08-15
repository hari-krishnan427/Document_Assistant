package com.docmind.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private String category;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    private String status = "ACTIVE";

    @Column(name = "is_encrypted")
    private Boolean isEncrypted = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public DocumentEntity() {}

    public DocumentEntity(Long id, User user, String fileName, String filePath, String fileType, Long fileSize, 
                          String category, String documentType, LocalDate issueDate, LocalDate expiryDate, 
                          String status, Boolean isEncrypted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.category = category;
        this.documentType = documentType;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.status = status != null ? status : "ACTIVE";
        this.isEncrypted = isEncrypted != null ? isEncrypted : false;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Boolean getIsEncrypted() { return isEncrypted; }
    public void setIsEncrypted(Boolean isEncrypted) { this.isEncrypted = isEncrypted; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static DocumentEntityBuilder builder() { return new DocumentEntityBuilder(); }

    public static class DocumentEntityBuilder {
        private Long id;
        private User user;
        private String fileName;
        private String filePath;
        private String fileType;
        private Long fileSize;
        private String category;
        private String documentType;
        private LocalDate issueDate;
        private LocalDate expiryDate;
        private String status = "ACTIVE";
        private Boolean isEncrypted = false;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public DocumentEntityBuilder id(Long id) { this.id = id; return this; }
        public DocumentEntityBuilder user(User user) { this.user = user; return this; }
        public DocumentEntityBuilder fileName(String fileName) { this.fileName = fileName; return this; }
        public DocumentEntityBuilder filePath(String filePath) { this.filePath = filePath; return this; }
        public DocumentEntityBuilder fileType(String fileType) { this.fileType = fileType; return this; }
        public DocumentEntityBuilder fileSize(Long fileSize) { this.fileSize = fileSize; return this; }
        public DocumentEntityBuilder category(String category) { this.category = category; return this; }
        public DocumentEntityBuilder documentType(String documentType) { this.documentType = documentType; return this; }
        public DocumentEntityBuilder issueDate(LocalDate issueDate) { this.issueDate = issueDate; return this; }
        public DocumentEntityBuilder expiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; return this; }
        public DocumentEntityBuilder status(String status) { this.status = status; return this; }
        public DocumentEntityBuilder isEncrypted(Boolean isEncrypted) { this.isEncrypted = isEncrypted; return this; }
        public DocumentEntityBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public DocumentEntityBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public DocumentEntity build() {
            return new DocumentEntity(id, user, fileName, filePath, fileType, fileSize, category, documentType, issueDate, expiryDate, status, isEncrypted, createdAt, updatedAt);
        }
    }
}
