package com.docmind.dto;

import java.time.LocalDate;

public class ExperienceDto {
    private Long id;
    private String companyName;
    private String jobTitle;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isCurrent;
    private String description;
    private Long verifiedByDocId;

    public ExperienceDto() {}

    public ExperienceDto(Long id, String companyName, String jobTitle, String location, LocalDate startDate, 
                         LocalDate endDate, Boolean isCurrent, String description, Long verifiedByDocId) {
        this.id = id;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isCurrent = isCurrent != null ? isCurrent : false;
        this.description = description;
        this.verifiedByDocId = verifiedByDocId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Long getVerifiedByDocId() { return verifiedByDocId; }
    public void setVerifiedByDocId(Long verifiedByDocId) { this.verifiedByDocId = verifiedByDocId; }

    public static ExperienceDtoBuilder builder() { return new ExperienceDtoBuilder(); }

    public static class ExperienceDtoBuilder {
        private Long id;
        private String companyName;
        private String jobTitle;
        private String location;
        private LocalDate startDate;
        private LocalDate endDate;
        private Boolean isCurrent = false;
        private String description;
        private Long verifiedByDocId;

        public ExperienceDtoBuilder id(Long id) { this.id = id; return this; }
        public ExperienceDtoBuilder companyName(String companyName) { this.companyName = companyName; return this; }
        public ExperienceDtoBuilder jobTitle(String jobTitle) { this.jobTitle = jobTitle; return this; }
        public ExperienceDtoBuilder location(String location) { this.location = location; return this; }
        public ExperienceDtoBuilder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public ExperienceDtoBuilder endDate(LocalDate endDate) { this.endDate = endDate; return this; }
        public ExperienceDtoBuilder isCurrent(Boolean isCurrent) { this.isCurrent = isCurrent; return this; }
        public ExperienceDtoBuilder description(String description) { this.description = description; return this; }
        public ExperienceDtoBuilder verifiedByDocId(Long verifiedByDocId) { this.verifiedByDocId = verifiedByDocId; return this; }

        public ExperienceDto build() {
            return new ExperienceDto(id, companyName, jobTitle, location, startDate, endDate, isCurrent, description, verifiedByDocId);
        }
    }
}
