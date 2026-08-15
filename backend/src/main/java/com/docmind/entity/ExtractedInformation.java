package com.docmind.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "extracted_information")
public class ExtractedInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private DocumentEntity document;

    @Column(name = "field_key", nullable = false)
    private String fieldKey;

    @Column(name = "field_value", columnDefinition = "TEXT", nullable = false)
    private String fieldValue;

    @Column(name = "confidence_score")
    private Float confidenceScore = 1.0f;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public ExtractedInformation() {}

    public ExtractedInformation(Long id, DocumentEntity document, String fieldKey, String fieldValue, 
                                Float confidenceScore, Boolean isVerified, LocalDateTime createdAt) {
        this.id = id;
        this.document = document;
        this.fieldKey = fieldKey;
        this.fieldValue = fieldValue;
        this.confidenceScore = confidenceScore != null ? confidenceScore : 1.0f;
        this.isVerified = isVerified != null ? isVerified : false;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public DocumentEntity getDocument() { return document; }
    public void setDocument(DocumentEntity document) { this.document = document; }

    public String getFieldKey() { return fieldKey; }
    public void setFieldKey(String fieldKey) { this.fieldKey = fieldKey; }

    public String getFieldValue() { return fieldValue; }
    public void setFieldValue(String fieldValue) { this.fieldValue = fieldValue; }

    public Float getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(Float confidenceScore) { this.confidenceScore = confidenceScore; }

    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static ExtractedInformationBuilder builder() { return new ExtractedInformationBuilder(); }

    public static class ExtractedInformationBuilder {
        private Long id;
        private DocumentEntity document;
        private String fieldKey;
        private String fieldValue;
        private Float confidenceScore = 1.0f;
        private Boolean isVerified = false;
        private LocalDateTime createdAt;

        public ExtractedInformationBuilder id(Long id) { this.id = id; return this; }
        public ExtractedInformationBuilder document(DocumentEntity document) { this.document = document; return this; }
        public ExtractedInformationBuilder fieldKey(String fieldKey) { this.fieldKey = fieldKey; return this; }
        public ExtractedInformationBuilder fieldValue(String fieldValue) { this.fieldValue = fieldValue; return this; }
        public ExtractedInformationBuilder confidenceScore(Float confidenceScore) { this.confidenceScore = confidenceScore; return this; }
        public ExtractedInformationBuilder isVerified(Boolean isVerified) { this.isVerified = isVerified; return this; }
        public ExtractedInformationBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public ExtractedInformation build() {
            return new ExtractedInformation(id, document, fieldKey, fieldValue, confidenceScore, isVerified, createdAt);
        }
    }
}
