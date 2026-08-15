package com.docmind.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "opportunities")
public class Opportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String organization;

    @Column(name = "opportunity_type", nullable = false)
    private String opportunityType; // JOB, GOVT_EXAM, INTERNSHIP, SCHOLARSHIP, COMPETITION

    @Column(columnDefinition = "TEXT")
    private String description;

    private String location;

    @Column(name = "salary_or_stipend")
    private String salaryOrStipend;

    private LocalDate deadline;

    @Column(name = "official_url")
    private String officialUrl;

    private String source;

    @Column(name = "published_date")
    private LocalDateTime publishedDate;

    @OneToMany(mappedBy = "opportunity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpportunityRequirement> requirements = new ArrayList<>();

    public Opportunity() {}

    public Opportunity(Long id, String title, String organization, String opportunityType, 
                       String description, String location, String salaryOrStipend, 
                       LocalDate deadline, String officialUrl, String source, 
                       LocalDateTime publishedDate, List<OpportunityRequirement> requirements) {
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
        this.requirements = requirements != null ? requirements : new ArrayList<>();
    }

    @PrePersist
    protected void onCreate() {
        if (this.publishedDate == null) {
            this.publishedDate = LocalDateTime.now();
        }
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

    public List<OpportunityRequirement> getRequirements() { return requirements; }
    public void setRequirements(List<OpportunityRequirement> requirements) { this.requirements = requirements; }

    public static OpportunityBuilder builder() { return new OpportunityBuilder(); }

    public static class OpportunityBuilder {
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
        private List<OpportunityRequirement> requirements = new ArrayList<>();

        public OpportunityBuilder id(Long id) { this.id = id; return this; }
        public OpportunityBuilder title(String title) { this.title = title; return this; }
        public OpportunityBuilder organization(String organization) { this.organization = organization; return this; }
        public OpportunityBuilder opportunityType(String opportunityType) { this.opportunityType = opportunityType; return this; }
        public OpportunityBuilder description(String description) { this.description = description; return this; }
        public OpportunityBuilder location(String location) { this.location = location; return this; }
        public OpportunityBuilder salaryOrStipend(String salaryOrStipend) { this.salaryOrStipend = salaryOrStipend; return this; }
        public OpportunityBuilder deadline(LocalDate deadline) { this.deadline = deadline; return this; }
        public OpportunityBuilder officialUrl(String officialUrl) { this.officialUrl = officialUrl; return this; }
        public OpportunityBuilder source(String source) { this.source = source; return this; }
        public OpportunityBuilder publishedDate(LocalDateTime publishedDate) { this.publishedDate = publishedDate; return this; }
        public OpportunityBuilder requirements(List<OpportunityRequirement> requirements) { this.requirements = requirements; return this; }

        public Opportunity build() {
            return new Opportunity(id, title, organization, opportunityType, description, location, salaryOrStipend, deadline, officialUrl, source, publishedDate, requirements);
        }
    }
}
