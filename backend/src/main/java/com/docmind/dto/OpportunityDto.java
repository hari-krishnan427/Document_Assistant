package com.docmind.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OpportunityDto {
    private Long id;
    private String title;
    private String organization;
    private String opportunityType;
    private String description;
    private String location;
    private String salaryOrStipend;
    private LocalDate deadline;
    private String officialUrl;
    private String source;
    private LocalDateTime publishedDate;
    private Integer matchScore;
    private String eligibilityStatus;
    private String matchedSkills;
    private String missingSkills;
    private String missingDocuments;
    private String aiRecommendation;

    public OpportunityDto() {}

    public OpportunityDto(Long id, String title, String organization, String opportunityType, 
                          String description, String location, String salaryOrStipend, 
                          LocalDate deadline, String officialUrl, String source, 
                          LocalDateTime publishedDate, Integer matchScore, String eligibilityStatus,
                          String matchedSkills, String missingSkills, String missingDocuments, String aiRecommendation) {
        this.id = id;
        this.title = title;
        this.organization = organization;
        this.opportunityType = opportunityType;
        this.description = description;
        this.location = location;
        this.salaryOrStipend = salaryOrStipend;
        this.deadline = deadline;
        this.officialUrl = officialUrl;
        this.source = source;
        this.publishedDate = publishedDate;
        this.matchScore = matchScore != null ? matchScore : 0;
        this.eligibilityStatus = eligibilityStatus != null ? eligibilityStatus : "ELIGIBLE";
        this.matchedSkills = matchedSkills;
        this.missingSkills = missingSkills;
        this.missingDocuments = missingDocuments;
        this.aiRecommendation = aiRecommendation;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }

    public String getOpportunityType() { return opportunityType; }
    public void setOpportunityType(String opportunityType) { this.opportunityType = opportunityType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getSalaryOrStipend() { return salaryOrStipend; }
    public void setSalaryOrStipend(String salaryOrStipend) { this.salaryOrStipend = salaryOrStipend; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public String getOfficialUrl() { return officialUrl; }
    public void setOfficialUrl(String officialUrl) { this.officialUrl = officialUrl; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public LocalDateTime getPublishedDate() { return publishedDate; }
    public void setPublishedDate(LocalDateTime publishedDate) { this.publishedDate = publishedDate; }

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

    public static OpportunityDtoBuilder builder() { return new OpportunityDtoBuilder(); }

    public static class OpportunityDtoBuilder {
        private Long id;
        private String title;
        private String organization;
        private String opportunityType;
        private String description;
        private String location;
        private String salaryOrStipend;
        private LocalDate deadline;
        private String officialUrl;
        private String source;
        private LocalDateTime publishedDate;
        private Integer matchScore = 0;
        private String eligibilityStatus = "ELIGIBLE";
        private String matchedSkills;
        private String missingSkills;
        private String missingDocuments;
        private String aiRecommendation;

        public OpportunityDtoBuilder id(Long id) { this.id = id; return this; }
        public OpportunityDtoBuilder title(String title) { this.title = title; return this; }
        public OpportunityDtoBuilder organization(String organization) { this.organization = organization; return this; }
        public OpportunityDtoBuilder opportunityType(String opportunityType) { this.opportunityType = opportunityType; return this; }
        public OpportunityDtoBuilder description(String description) { this.description = description; return this; }
        public OpportunityDtoBuilder location(String location) { this.location = location; return this; }
        public OpportunityDtoBuilder salaryOrStipend(String salaryOrStipend) { this.salaryOrStipend = salaryOrStipend; return this; }
        public OpportunityDtoBuilder deadline(LocalDate deadline) { this.deadline = deadline; return this; }
        public OpportunityDtoBuilder officialUrl(String officialUrl) { this.officialUrl = officialUrl; return this; }
        public OpportunityDtoBuilder source(String source) { this.source = source; return this; }
        public OpportunityDtoBuilder publishedDate(LocalDateTime publishedDate) { this.publishedDate = publishedDate; return this; }
        public OpportunityDtoBuilder matchScore(Integer matchScore) { this.matchScore = matchScore; return this; }
        public OpportunityDtoBuilder eligibilityStatus(String eligibilityStatus) { this.eligibilityStatus = eligibilityStatus; return this; }
        public OpportunityDtoBuilder matchedSkills(String matchedSkills) { this.matchedSkills = matchedSkills; return this; }
        public OpportunityDtoBuilder missingSkills(String missingSkills) { this.missingSkills = missingSkills; return this; }
        public OpportunityDtoBuilder missingDocuments(String missingDocuments) { this.missingDocuments = missingDocuments; return this; }
        public OpportunityDtoBuilder aiRecommendation(String aiRecommendation) { this.aiRecommendation = aiRecommendation; return this; }

        public OpportunityDto build() {
            return new OpportunityDto(id, title, organization, opportunityType, description, location, salaryOrStipend, deadline, officialUrl, source, publishedDate, matchScore, eligibilityStatus, matchedSkills, missingSkills, missingDocuments, aiRecommendation);
        }
    }
}
