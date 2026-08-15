package com.docmind.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "opportunity_matches", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "opportunity_id"})
})
public class OpportunityMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opportunity_id", nullable = false)
    private Opportunity opportunity;

    @Column(name = "match_score", nullable = false)
    private Integer matchScore; // 0 to 100

    @Column(name = "eligibility_status", nullable = false)
    private String eligibilityStatus; // ELIGIBLE, PARTIALLY_ELIGIBLE, NOT_ELIGIBLE

    @Column(name = "matched_skills", columnDefinition = "TEXT")
    private String matchedSkills;

    @Column(name = "missing_skills", columnDefinition = "TEXT")
    private String missingSkills;

    @Column(name = "missing_documents", columnDefinition = "TEXT")
    private String missingDocuments;

    @Column(name = "ai_recommendation", columnDefinition = "TEXT")
    private String aiRecommendation;

    @Column(name = "analyzed_at")
    private LocalDateTime analyzedAt;

    public OpportunityMatch() {}

    public OpportunityMatch(Long id, User user, Opportunity opportunity, Integer matchScore, 
                            String eligibilityStatus, String matchedSkills, String missingSkills, 
                            String missingDocuments, String aiRecommendation, LocalDateTime analyzedAt) {
        this.id = id;
        this.user = user;
        this.opportunity = opportunity;
        this.matchScore = matchScore != null ? matchScore : 0;
        this.eligibilityStatus = eligibilityStatus;
        this.matchedSkills = matchedSkills;
        this.missingSkills = missingSkills;
        this.missingDocuments = missingDocuments;
        this.aiRecommendation = aiRecommendation;
        this.analyzedAt = analyzedAt;
    }

    @PrePersist
    protected void onCreate() {
        this.analyzedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Opportunity getOpportunity() { return opportunity; }
    public void setOpportunity(Opportunity opportunity) { this.opportunity = opportunity; }

    public Integer getMatchScore() { return matchScore; }
    public void setMatchScore(Integer matchScore) { this.matchScore = matchScore; }

    public String getEligibilityStatus() { return eligibilityStatus; }
    public void setEligibilityStatus(String eligibilityStatus) { this.eligibilityStatus = eligibilityStatus; }

    public String getMatchedSkills() { return matchedSkills; }
    public void setMatchedSkills(String matchedSkills) { this.matchedSkills = matchedSkills; }

    public String getMissingSkills() { return missingSkills; }
    public void setMissingSkills(String missingSkills) { this.missingSkills = missingSkills; }

    public String getMissingDocuments() { return missingDocuments; }
    public void setMissingDocuments(String missingDocuments) { this.missingDocuments = missingDocuments; }

    public String getAiRecommendation() { return aiRecommendation; }
    public void setAiRecommendation(String aiRecommendation) { this.aiRecommendation = aiRecommendation; }

    public LocalDateTime getAnalyzedAt() { return analyzedAt; }
    public void setAnalyzedAt(LocalDateTime analyzedAt) { this.analyzedAt = analyzedAt; }

    public static OpportunityMatchBuilder builder() { return new OpportunityMatchBuilder(); }

    public static class OpportunityMatchBuilder {
        private Long id;
        private User user;
        private Opportunity opportunity;
        private Integer matchScore = 0;
        private String eligibilityStatus;
        private String matchedSkills;
        private String missingSkills;
        private String missingDocuments;
        private String aiRecommendation;
        private LocalDateTime analyzedAt;

        public OpportunityMatchBuilder id(Long id) { this.id = id; return this; }
        public OpportunityMatchBuilder user(User user) { this.user = user; return this; }
        public OpportunityMatchBuilder opportunity(Opportunity opportunity) { this.opportunity = opportunity; return this; }
        public OpportunityMatchBuilder matchScore(Integer matchScore) { this.matchScore = matchScore; return this; }
        public OpportunityMatchBuilder eligibilityStatus(String eligibilityStatus) { this.eligibilityStatus = eligibilityStatus; return this; }
        public OpportunityMatchBuilder matchedSkills(String matchedSkills) { this.matchedSkills = matchedSkills; return this; }
        public OpportunityMatchBuilder missingSkills(String missingSkills) { this.missingSkills = missingSkills; return this; }
        public OpportunityMatchBuilder missingDocuments(String missingDocuments) { this.missingDocuments = missingDocuments; return this; }
        public OpportunityMatchBuilder aiRecommendation(String aiRecommendation) { this.aiRecommendation = aiRecommendation; return this; }
        public OpportunityMatchBuilder analyzedAt(LocalDateTime analyzedAt) { this.analyzedAt = analyzedAt; return this; }

        public OpportunityMatch build() {
            return new OpportunityMatch(id, user, opportunity, matchScore, eligibilityStatus, matchedSkills, missingSkills, missingDocuments, aiRecommendation, analyzedAt);
        }
    }
}
