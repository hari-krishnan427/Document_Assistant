package com.docmind.dto;

import java.time.LocalDateTime;

public class DocumentBundleDto {
    private Long id;
    private Long userId;
    private Long opportunityId;
    private String opportunityTitle;
    private String bundleName;
    private String bundlePath;
    private Integer fileCount;
    private LocalDateTime createdAt;

    public DocumentBundleDto() {}

    public DocumentBundleDto(Long id, Long userId, Long opportunityId, String opportunityTitle, 
                             String bundleName, String bundlePath, Integer fileCount, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.opportunityId = opportunityId;
        this.opportunityTitle = opportunityTitle;
        this.bundleName = bundleName;
        this.bundlePath = bundlePath;
        this.fileCount = fileCount != null ? fileCount : 0;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getOpportunityId() { return opportunityId; }
    public void setOpportunityId(Long opportunityId) { this.opportunityId = opportunityId; }

    public String getOpportunityTitle() { return opportunityTitle; }
    public void setOpportunityTitle(String opportunityTitle) { this.opportunityTitle = opportunityTitle; }

    public String getBundleName() { return bundleName; }
    public void setBundleName(String bundleName) { this.bundleName = bundleName; }

    public String getBundlePath() { return bundlePath; }
    public void setBundlePath(String bundlePath) { this.bundlePath = bundlePath; }

    public Integer getFileCount() { return fileCount; }
    public void setFileCount(Integer fileCount) { this.fileCount = fileCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static DocumentBundleDtoBuilder builder() { return new DocumentBundleDtoBuilder(); }

    public static class DocumentBundleDtoBuilder {
        private Long id;
        private Long userId;
        private Long opportunityId;
        private String opportunityTitle;
        private String bundleName;
        private String bundlePath;
        private Integer fileCount = 0;
        private LocalDateTime createdAt;

        public DocumentBundleDtoBuilder id(Long id) { this.id = id; return this; }
        public DocumentBundleDtoBuilder userId(Long userId) { this.userId = userId; return this; }
        public DocumentBundleDtoBuilder opportunityId(Long opportunityId) { this.opportunityId = opportunityId; return this; }
        public DocumentBundleDtoBuilder opportunityTitle(String opportunityTitle) { this.opportunityTitle = opportunityTitle; return this; }
        public DocumentBundleDtoBuilder bundleName(String bundleName) { this.bundleName = bundleName; return this; }
        public DocumentBundleDtoBuilder bundlePath(String bundlePath) { this.bundlePath = bundlePath; return this; }
        public DocumentBundleDtoBuilder fileCount(Integer fileCount) { this.fileCount = fileCount; return this; }
        public DocumentBundleDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public DocumentBundleDto build() {
            return new DocumentBundleDto(id, userId, opportunityId, opportunityTitle, bundleName, bundlePath, fileCount, createdAt);
        }
    }
}
