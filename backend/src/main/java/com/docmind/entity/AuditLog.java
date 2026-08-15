package com.docmind.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "action_type", nullable = false)
    private String actionType;

    private String resource;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public AuditLog() {}

    public AuditLog(Long id, User user, String actionType, String resource, String details, String ipAddress, String userAgent, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.actionType = actionType;
        this.resource = resource;
        this.details = details;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
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

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }

    public String getResource() { return resource; }
    public void setResource(String resource) { this.resource = resource; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static AuditLogBuilder builder() { return new AuditLogBuilder(); }

    public static class AuditLogBuilder {
        private Long id;
        private User user;
        private String actionType;
        private String resource;
        private String details;
        private String ipAddress;
        private String userAgent;
        private LocalDateTime createdAt;

        public AuditLogBuilder id(Long id) { this.id = id; return this; }
        public AuditLogBuilder user(User user) { this.user = user; return this; }
        public AuditLogBuilder actionType(String actionType) { this.actionType = actionType; return this; }
        public AuditLogBuilder resource(String resource) { this.resource = resource; return this; }
        public AuditLogBuilder details(String details) { this.details = details; return this; }
        public AuditLogBuilder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public AuditLogBuilder userAgent(String userAgent) { this.userAgent = userAgent; return this; }
        public AuditLogBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public AuditLog build() {
            return new AuditLog(id, user, actionType, resource, details, ipAddress, userAgent, createdAt);
        }
    }
}
