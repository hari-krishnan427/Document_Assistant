package com.docmind.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "skill_name", nullable = false)
    private String skillName;

    @Column(name = "proficiency_level")
    private String proficiencyLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by_doc_id")
    private DocumentEntity verifiedByDoc;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Skill() {}

    public Skill(Long id, User user, String skillName, String proficiencyLevel, DocumentEntity verifiedByDoc, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.skillName = skillName;
        this.proficiencyLevel = proficiencyLevel;
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

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public String getProficiencyLevel() { return proficiencyLevel; }
    public void setProficiencyLevel(String proficiencyLevel) { this.proficiencyLevel = proficiencyLevel; }

    public DocumentEntity getVerifiedByDoc() { return verifiedByDoc; }
    public void setVerifiedByDoc(DocumentEntity verifiedByDoc) { this.verifiedByDoc = verifiedByDoc; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static SkillBuilder builder() { return new SkillBuilder(); }

    public static class SkillBuilder {
        private Long id;
        private User user;
        private String skillName;
        private String proficiencyLevel;
        private DocumentEntity verifiedByDoc;
        private LocalDateTime createdAt;

        public SkillBuilder id(Long id) { this.id = id; return this; }
        public SkillBuilder user(User user) { this.user = user; return this; }
        public SkillBuilder skillName(String skillName) { this.skillName = skillName; return this; }
        public SkillBuilder proficiencyLevel(String proficiencyLevel) { this.proficiencyLevel = proficiencyLevel; return this; }
        public SkillBuilder verifiedByDoc(DocumentEntity verifiedByDoc) { this.verifiedByDoc = verifiedByDoc; return this; }
        public SkillBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Skill build() {
            return new Skill(id, user, skillName, proficiencyLevel, verifiedByDoc, createdAt);
        }
    }
}
