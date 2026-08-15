package com.docmind.dto;

import java.time.LocalDateTime;

public class ExtractedInformationDto {
    private Long id;
    private Long documentId;
    private String fieldKey;
    private String fieldValue;
    private Float confidenceScore;
    private Boolean isVerified;
    private LocalDateTime createdAt;

    public ExtractedInformationDto() {}

    public ExtractedInformationDto(Long id, Long documentId, String fieldKey, String fieldValue, Float confidenceScore, Boolean isVerified, LocalDateTime createdAt) {
        this.id = id;
        this.documentId = documentId;
        this.fieldKey = fieldKey;
        this.fieldValue = fieldValue;
        this.confidenceScore = confidenceScore;
        this.isVerified = isVerified;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDocumentId() { return documentId; }
    public void setDocumentId(Long documentId) { this.documentId = documentId; }

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

    public static ExtractedInformationDtoBuilder builder() { return new ExtractedInformationDtoBuilder(); }

    public static class ExtractedInformationDtoBuilder {
        private Long id;
        private Long documentId;
        private String fieldKey;
        private String fieldValue;
        private Float confidenceScore;
        private Boolean isVerified;
        private LocalDateTime createdAt;

        public ExtractedInformationDtoBuilder id(Long id) { this.id = id; return this; }
        public ExtractedInformationDtoBuilder documentId(Long documentId) { this.documentId = documentId; return this; }
        public ExtractedInformationDtoBuilder fieldKey(String fieldKey) { this.fieldKey = fieldKey; return this; }
        public ExtractedInformationDtoBuilder fieldValue(String fieldValue) { this.fieldValue = fieldValue; return this; }
        public ExtractedInformationDtoBuilder confidenceScore(Float confidenceScore) { this.confidenceScore = confidenceScore; return this; }
        public ExtractedInformationDtoBuilder isVerified(Boolean isVerified) { this.isVerified = isVerified; return this; }
        public ExtractedInformationDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public ExtractedInformationDto build() {
            return new ExtractedInformationDto(id, documentId, fieldKey, fieldValue, confidenceScore, isVerified, createdAt);
        }
    }
}
