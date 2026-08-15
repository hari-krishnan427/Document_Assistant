package com.docmind.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_experience")
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "location")
    private String location;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_current")
    private Boolean isCurrent = false;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by_doc_id")
    private DocumentEntity verifiedByDoc;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Experience() {}

    public Experience(Long id, User user, String companyName, String jobTitle, String location, 
                      LocalDate startDate, LocalDate endDate, Boolean isCurrent, String description, 
                      DocumentEntity verifiedByDoc, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isCurrent = isCurrent != null ? isCurrent : false;
        this.description = description;
        this.verifiedByDoc = verifiedByDoc;
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

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Boolean getIsCurrent() { return isCurrent; }
    public void setIsCurrent(Boolean isCurrent) { this.isCurrent = isCurrent; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public DocumentEntity getVerifiedByDoc() { return verifiedByDoc; }
    public void setVerifiedByDoc(DocumentEntity verifiedByDoc) { this.verifiedByDoc = verifiedByDoc; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static ExperienceBuilder builder() { return new ExperienceBuilder(); }

    public static class ExperienceBuilder {
        private Long id;
        private User user;
        private String companyName;
        private String jobTitle;
        private String location;
        private LocalDate startDate;
        private LocalDate endDate;
        private Boolean isCurrent = false;
        private String description;
        private DocumentEntity verifiedByDoc;
        private LocalDateTime createdAt;

        public ExperienceBuilder id(Long id) { this.id = id; return this; }
        public ExperienceBuilder user(User user) { this.user = user; return this; }
        public ExperienceBuilder companyName(String companyName) { this.companyName = companyName; return this; }
        public ExperienceBuilder jobTitle(String jobTitle) { this.jobTitle = jobTitle; return this; }
        public ExperienceBuilder location(String location) { this.location = location; return this; }
        public ExperienceBuilder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public ExperienceBuilder endDate(LocalDate endDate) { this.endDate = endDate; return this; }
        public ExperienceBuilder isCurrent(Boolean isCurrent) { this.isCurrent = isCurrent; return this; }
        public ExperienceBuilder description(String description) { this.description = description; return this; }
        public ExperienceBuilder verifiedByDoc(DocumentEntity verifiedByDoc) { this.verifiedByDoc = verifiedByDoc; return this; }
        public ExperienceBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Experience build() {
            return new Experience(id, user, companyName, jobTitle, location, startDate, endDate, isCurrent, description, verifiedByDoc, createdAt);
        }
    }
}
