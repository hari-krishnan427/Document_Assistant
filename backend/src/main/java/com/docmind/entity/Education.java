package com.docmind.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_education")
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "institution_name", nullable = false)
    private String institutionName;

    @Column(name = "degree", nullable = false)
    private String degree;

    @Column(name = "field_of_study")
    private String fieldOfStudy;

    @Column(name = "start_year")
    private Integer startYear;

    @Column(name = "end_year")
    private Integer endYear;

    @Column(name = "grade_or_cgpa")
    private String gradeOrCgpa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by_doc_id")
    private DocumentEntity verifiedByDoc;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Education() {}

    public Education(Long id, User user, String institutionName, String degree, String fieldOfStudy,
                     Integer startYear, Integer endYear, String gradeOrCgpa, DocumentEntity verifiedByDoc, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.institutionName = institutionName;
        this.degree = degree;
        this.fieldOfStudy = fieldOfStudy;
        this.startYear = startYear;
        this.endYear = endYear;
        this.gradeOrCgpa = gradeOrCgpa;
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

    public DocumentEntity getVerifiedByDoc() { return verifiedByDoc; }
    public void setVerifiedByDoc(DocumentEntity verifiedByDoc) { this.verifiedByDoc = verifiedByDoc; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static EducationBuilder builder() { return new EducationBuilder(); }

    public static class EducationBuilder {
        private Long id;
        private User user;
        private String institutionName;
        private String degree;
        private String fieldOfStudy;
        private Integer startYear;
        private Integer endYear;
        private String gradeOrCgpa;
        private DocumentEntity verifiedByDoc;
        private LocalDateTime createdAt;

        public EducationBuilder id(Long id) { this.id = id; return this; }
        public EducationBuilder user(User user) { this.user = user; return this; }
        public EducationBuilder institutionName(String institutionName) { this.institutionName = institutionName; return this; }
        public EducationBuilder degree(String degree) { this.degree = degree; return this; }
        public EducationBuilder fieldOfStudy(String fieldOfStudy) { this.fieldOfStudy = fieldOfStudy; return this; }
        public EducationBuilder startYear(Integer startYear) { this.startYear = startYear; return this; }
        public EducationBuilder endYear(Integer endYear) { this.endYear = endYear; return this; }
        public EducationBuilder gradeOrCgpa(String gradeOrCgpa) { this.gradeOrCgpa = gradeOrCgpa; return this; }
        public EducationBuilder verifiedByDoc(DocumentEntity verifiedByDoc) { this.verifiedByDoc = verifiedByDoc; return this; }
        public EducationBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Education build() {
            return new Education(id, user, institutionName, degree, fieldOfStudy, startYear, endYear, gradeOrCgpa, verifiedByDoc, createdAt);
        }
    }
}
