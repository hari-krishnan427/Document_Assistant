package com.docmind.dto;

import java.time.LocalDateTime;

public class AuditLogDto {
    private Long id;
    private Long userId;
    private String userName;
    private String actionType;
    private String resource;
    private String details;
    private String ipAddress;
    private LocalDateTime createdAt;

    public AuditLogDto() {}

    public AuditLogDto(Long id, Long userId, String userName, String actionType, String resource, 
                       String details, String ipAddress, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.actionType = actionType;
        this.resource = resource;
        this.details = details;
        this.ipAddress = ipAddress;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }

    public String getResource() { return resource; }
    public void setResource(String resource) { this.resource = resource; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static AuditLogDtoBuilder builder() { return new AuditLogDtoBuilder(); }

    public static class AuditLogDtoBuilder {
        private Long id;
        private Long userId;
        private String userName;
        private String actionType;
        private String resource;
        private String details;
        private String ipAddress;
        private LocalDateTime createdAt;

        public AuditLogDtoBuilder id(Long id) { this.id = id; return this; }
        public AuditLogDtoBuilder userId(Long userId) { this.userId = userId; return this; }
        public AuditLogDtoBuilder userName(String userName) { this.userName = userName; return this; }
        public AuditLogDtoBuilder actionType(String actionType) { this.actionType = actionType; return this; }
        public AuditLogDtoBuilder resource(String resource) { this.resource = resource; return this; }
        public AuditLogDtoBuilder details(String details) { this.details = details; return this; }
        public AuditLogDtoBuilder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public AuditLogDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public AuditLogDto build() {
            return new AuditLogDto(id, userId, userName, actionType, resource, details, ipAddress, createdAt);
        }
    }
}
