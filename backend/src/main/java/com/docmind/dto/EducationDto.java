package com.docmind.dto;

public class EducationDto {
    private Long id;
    private String institutionName;
    private String degree;
    private String fieldOfStudy;
    private Integer startYear;
    private Integer endYear;
    private String gradeOrCgpa;
    private Long verifiedByDocId;

    public EducationDto() {}

    public EducationDto(Long id, String institutionName, String degree, String fieldOfStudy, 
                        Integer startYear, Integer endYear, String gradeOrCgpa, Long verifiedByDocId) {
        this.id = id;
        this.institutionName = institutionName;
        this.degree = degree;
        this.fieldOfStudy = fieldOfStudy;
        this.startYear = startYear;
        this.endYear = endYear;
        this.gradeOrCgpa = gradeOrCgpa;
        this.verifiedByDocId = verifiedByDocId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getInstitutionName() { return institutionName; }
    public void setInstitutionName(String institutionName) { this.institutionName = institutionName; }

    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }

    public String getFieldOfStudy() { return fieldOfStudy; }
    public void setFieldOfStudy(String fieldOfStudy) { this.fieldOfStudy = fieldOfStudy; }

    public Integer getStartYear() { return startYear; }
    public void setStartYear(Integer startYear) { this.startYear = startYear; }

    public Integer getEndYear() { return endYear; }
    public void setEndYear(Integer endYear) { this.endYear = endYear; }

    public String getGradeOrCgpa() { return gradeOrCgpa; }
    public void setGradeOrCgpa(String gradeOrCgpa) { this.gradeOrCgpa = gradeOrCgpa; }

    public Long getVerifiedByDocId() { return verifiedByDocId; }
    public void setVerifiedByDocId(Long verifiedByDocId) { this.verifiedByDocId = verifiedByDocId; }

    public static EducationDtoBuilder builder() { return new EducationDtoBuilder(); }

    public static class EducationDtoBuilder {
        private Long id;
        private String institutionName;
        private String degree;
        private String fieldOfStudy;
        private Integer startYear;
        private Integer endYear;
        private String gradeOrCgpa;
        private Long verifiedByDocId;

        public EducationDtoBuilder id(Long id) { this.id = id; return this; }
        public EducationDtoBuilder institutionName(String institutionName) { this.institutionName = institutionName; return this; }
        public EducationDtoBuilder degree(String degree) { this.degree = degree; return this; }
        public EducationDtoBuilder fieldOfStudy(String fieldOfStudy) { this.fieldOfStudy = fieldOfStudy; return this; }
        public EducationDtoBuilder startYear(Integer startYear) { this.startYear = startYear; return this; }
        public EducationDtoBuilder endYear(Integer endYear) { this.endYear = endYear; return this; }
        public EducationDtoBuilder gradeOrCgpa(String gradeOrCgpa) { this.gradeOrCgpa = gradeOrCgpa; return this; }
        public EducationDtoBuilder verifiedByDocId(Long verifiedByDocId) { this.verifiedByDocId = verifiedByDocId; return this; }

        public EducationDto build() {
            return new EducationDto(id, institutionName, degree, fieldOfStudy, startYear, endYear, gradeOrCgpa, verifiedByDocId);
        }
    }
}
