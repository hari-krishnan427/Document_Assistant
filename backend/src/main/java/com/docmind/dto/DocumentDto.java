package com.docmind.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DocumentDto {
    private Long id;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String category;
    private String documentType;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String status;
    private Boolean isEncrypted;
    private LocalDateTime createdAt;

    public DocumentDto() {}

    public DocumentDto(Long id, String fileName, String fileType, Long fileSize, String category, 
                       String documentType, LocalDate issueDate, LocalDate expiryDate, 
                       String status, Boolean isEncrypted, LocalDateTime createdAt) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.category = category;
        this.documentType = documentType;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.status = status;
        this.isEncrypted = isEncrypted;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

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

    public static DocumentDtoBuilder builder() { return new DocumentDtoBuilder(); }

    public static class DocumentDtoBuilder {
        private Long id;
        private String fileName;
        private String fileType;
        private Long fileSize;
        private String category;
        private String documentType;
        private LocalDate issueDate;
        private LocalDate expiryDate;
        private String status;
        private Boolean isEncrypted;
        private LocalDateTime createdAt;

        public DocumentDtoBuilder id(Long id) { this.id = id; return this; }
        public DocumentDtoBuilder fileName(String fileName) { this.fileName = fileName; return this; }
        public DocumentDtoBuilder fileType(String fileType) { this.fileType = fileType; return this; }
        public DocumentDtoBuilder fileSize(Long fileSize) { this.fileSize = fileSize; return this; }
        public DocumentDtoBuilder category(String category) { this.category = category; return this; }
        public DocumentDtoBuilder documentType(String documentType) { this.documentType = documentType; return this; }
        public DocumentDtoBuilder issueDate(LocalDate issueDate) { this.issueDate = issueDate; return this; }
        public DocumentDtoBuilder expiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; return this; }
        public DocumentDtoBuilder status(String status) { this.status = status; return this; }
        public DocumentDtoBuilder isEncrypted(Boolean isEncrypted) { this.isEncrypted = isEncrypted; return this; }
        public DocumentDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public DocumentDto build() {
            return new DocumentDto(id, fileName, fileType, fileSize, category, documentType, issueDate, expiryDate, status, isEncrypted, createdAt);
        }
    }
}
