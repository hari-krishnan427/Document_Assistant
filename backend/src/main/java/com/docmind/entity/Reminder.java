package com.docmind.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reminders")
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private DocumentEntity document;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "reminder_date", nullable = false)
    private LocalDate reminderDate;

    private String priority = "MEDIUM"; // LOW, MEDIUM, HIGH, URGENT

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Reminder() {}

    public Reminder(Long id, User user, DocumentEntity document, String title, String message, 
                    LocalDate reminderDate, String priority, Boolean isRead, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.document = document;
        this.title = title;
        this.message = message;
        this.reminderDate = reminderDate;
        this.priority = priority != null ? priority : "MEDIUM";
        this.isRead = isRead != null ? isRead : false;
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

    public DocumentEntity getDocument() { return document; }
    public void setDocument(DocumentEntity document) { this.document = document; }

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

    public static ReminderBuilder builder() { return new ReminderBuilder(); }

    public static class ReminderBuilder {
        private Long id;
        private User user;
        private DocumentEntity document;
        private String title;
        private String message;
        private LocalDate reminderDate;
        private String priority = "MEDIUM";
        private Boolean isRead = false;
        private LocalDateTime createdAt;

        public ReminderBuilder id(Long id) { this.id = id; return this; }
        public ReminderBuilder user(User user) { this.user = user; return this; }
        public ReminderBuilder document(DocumentEntity document) { this.document = document; return this; }
        public ReminderBuilder title(String title) { this.title = title; return this; }
        public ReminderBuilder message(String message) { this.message = message; return this; }
        public ReminderBuilder reminderDate(LocalDate reminderDate) { this.reminderDate = reminderDate; return this; }
        public ReminderBuilder priority(String priority) { this.priority = priority; return this; }
        public ReminderBuilder isRead(Boolean isRead) { this.isRead = isRead; return this; }
        public ReminderBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Reminder build() {
            return new Reminder(id, user, document, title, message, reminderDate, priority, isRead, createdAt);
        }
    }
}
