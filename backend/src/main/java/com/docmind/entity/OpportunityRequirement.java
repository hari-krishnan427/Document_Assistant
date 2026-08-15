package com.docmind.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "opportunity_requirements")
public class OpportunityRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opportunity_id", nullable = false)
    private Opportunity opportunity;

    @Column(name = "requirement_type", nullable = false)
    private String requirementType; // SKILL, DEGREE, EXPERIENCE, AGE, DOCUMENT

    @Column(name = "requirement_value", nullable = false)
    private String requirementValue;

    @Column(name = "is_mandatory")
    private Boolean isMandatory = true;

    public OpportunityRequirement() {}

    public OpportunityRequirement(Long id, Opportunity opportunity, String requirementType, String requirementValue, Boolean isMandatory) {
        this.id = id;
        this.opportunity = opportunity;
        this.requirementType = requirementType;
        this.requirementValue = requirementValue;
        this.isMandatory = isMandatory != null ? isMandatory : true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Opportunity getOpportunity() { return opportunity; }
    public void setOpportunity(Opportunity opportunity) { this.opportunity = opportunity; }

    public String getRequirementType() { return requirementType; }
    public void setRequirementType(String requirementType) { this.requirementType = requirementType; }

    public String getRequirementValue() { return requirementValue; }
    public void setRequirementValue(String requirementValue) { this.requirementValue = requirementValue; }

    public Boolean getIsMandatory() { return isMandatory; }
    public void setIsMandatory(Boolean isMandatory) { this.isMandatory = isMandatory; }

    public static OpportunityRequirementBuilder builder() { return new OpportunityRequirementBuilder(); }

    public static class OpportunityRequirementBuilder {
        private Long id;
        private Opportunity opportunity;
        private String requirementType;
        private String requirementValue;
        private Boolean isMandatory = true;

        public OpportunityRequirementBuilder id(Long id) { this.id = id; return this; }
        public OpportunityRequirementBuilder opportunity(Opportunity opportunity) { this.opportunity = opportunity; return this; }
        public OpportunityRequirementBuilder requirementType(String requirementType) { this.requirementType = requirementType; return this; }
        public OpportunityRequirementBuilder requirementValue(String requirementValue) { this.requirementValue = requirementValue; return this; }
        public OpportunityRequirementBuilder isMandatory(Boolean isMandatory) { this.isMandatory = isMandatory; return this; }

        public OpportunityRequirement build() {
            return new OpportunityRequirement(id, opportunity, requirementType, requirementValue, isMandatory);
        }
    }
}
