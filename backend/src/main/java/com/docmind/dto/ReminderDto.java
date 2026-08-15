package com.docmind.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReminderDto {
    private Long id;
    private Long userId;
    private Long documentId;
    private String documentName;
    private String title;
    private String message;
    private LocalDate reminderDate;
    private String priority;
    private Boolean isRead;
    private LocalDateTime createdAt;

    public ReminderDto() {}

    public ReminderDto(Long id, Long userId, Long documentId, String documentName, String title, 
                       String message, LocalDate reminderDate, String priority, Boolean isRead, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.documentId = documentId;
        this.documentName = documentName;
        this.title = title;
        this.message = message;
        this.reminderDate = reminderDate;
        this.priority = priority != null ? priority : "MEDIUM";
        this.isRead = isRead != null ? isRead : false;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getDocumentId() { return documentId; }
    public void setDocumentId(Long documentId) { this.documentId = documentId; }

    public String getDocumentName() { return documentName; }
    public void setDocumentName(String documentName) { this.documentName = documentName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDate getReminderDate() { return reminderDate; }
    public void setReminderDate(LocalDate reminderDate) { this.reminderDate = reminderDate; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static ReminderDtoBuilder builder() { return new ReminderDtoBuilder(); }

    public static class ReminderDtoBuilder {
        private Long id;
        private Long userId;
        private Long documentId;
        private String documentName;
        private String title;
        private String message;
        private LocalDate reminderDate;
        private String priority = "MEDIUM";
        private Boolean isRead = false;
        private LocalDateTime createdAt;

        public ReminderDtoBuilder id(Long id) { this.id = id; return this; }
        public ReminderDtoBuilder userId(Long userId) { this.userId = userId; return this; }
        public ReminderDtoBuilder documentId(Long documentId) { this.documentId = documentId; return this; }
        public ReminderDtoBuilder documentName(String documentName) { this.documentName = documentName; return this; }
        public ReminderDtoBuilder title(String title) { this.title = title; return this; }
        public ReminderDtoBuilder message(String message) { this.message = message; return this; }
        public ReminderDtoBuilder reminderDate(LocalDate reminderDate) { this.reminderDate = reminderDate; return this; }
        public ReminderDtoBuilder priority(String priority) { this.priority = priority; return this; }
        public ReminderDtoBuilder isRead(Boolean isRead) { this.isRead = isRead; return this; }
        public ReminderDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public ReminderDto build() {
            return new ReminderDto(id, userId, documentId, documentName, title, message, reminderDate, priority, isRead, createdAt);
        }
    }
}
