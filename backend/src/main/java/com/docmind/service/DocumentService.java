package com.docmind.service;

import com.docmind.dto.DocumentDto;
import com.docmind.dto.ExtractedInformationDto;
import com.docmind.entity.*;
import com.docmind.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final ExtractedInformationRepository extractedInformationRepository;
    private final SkillRepository skillRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;
    private final CertificationRepository certificationRepository;
    private final AuditLogService auditLogService;
    private final ExtractedInformationService extractedInformationService;
    private final Path fileStorageLocation;

    public DocumentService(DocumentRepository documentRepository,
                           ExtractedInformationRepository extractedInformationRepository,
                           SkillRepository skillRepository,
                           EducationRepository educationRepository,
                           ExperienceRepository experienceRepository,
                           CertificationRepository certificationRepository,
                           AuditLogService auditLogService,
                           ExtractedInformationService extractedInformationService,
                           @Value("${app.upload.dir:uploads}") String uploadDir) {
        this.documentRepository = documentRepository;
        this.extractedInformationRepository = extractedInformationRepository;
        this.skillRepository = skillRepository;
        this.educationRepository = educationRepository;
        this.experienceRepository = experienceRepository;
        this.certificationRepository = certificationRepository;
        this.auditLogService = auditLogService;
        this.extractedInformationService = extractedInformationService;
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the upload directory for documents.", ex);
        }
    }

    @Transactional
    public DocumentDto uploadDocument(User user, MultipartFile file, String category, 
                                       String documentType, LocalDate issueDate, LocalDate expiryDate) {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        if (originalFileName.contains("..")) {
            throw new IllegalArgumentException("Filename contains invalid path sequence: " + originalFileName);
        }

        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("application/pdf") 
                && !contentType.startsWith("image/"))) {
            throw new IllegalArgumentException("Unsupported document format. Please upload PDF, JPG, or PNG files.");
        }

        String fileExtension = "";
        int lastDotIndex = originalFileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            fileExtension = originalFileName.substring(lastDotIndex);
        }
        String storedFileName = UUID.randomUUID().toString() + fileExtension;
        Path targetLocation = this.fileStorageLocation.resolve(storedFileName);

        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + originalFileName + ". Please try again!", ex);
        }

        String finalCategory = (category != null && !category.trim().isEmpty()) ? category : autoDetectCategory(originalFileName);
        String finalDocType = (documentType != null && !documentType.trim().isEmpty()) ? documentType : autoDetectDocType(originalFileName);
        String status = computeStatus(expiryDate);

        DocumentEntity document = DocumentEntity.builder()
                .user(user)
                .fileName(originalFileName)
                .filePath(targetLocation.toString())
                .fileType(contentType)
                .fileSize(file.getSize())
                .category(finalCategory)
                .documentType(finalDocType)
                .issueDate(issueDate)
                .expiryDate(expiryDate)
                .status(status)
                .isEncrypted(true)
                .build();

        DocumentEntity savedDoc = documentRepository.save(document);

        // Run OCR & Extract key-value entities
        extractedInformationService.processAndStoreExtractedInfo(savedDoc);

        auditLogService.logAction(user, "UPLOAD", "Document: " + originalFileName, 
                "Uploaded document under category: " + finalCategory + ", OCR processed", null, null);

        return mapToDto(savedDoc);
    }

    @Transactional(readOnly = true)
    public List<DocumentDto> getUserDocuments(Long userId, String category, String status) {
        List<DocumentEntity> docs;
        if (category != null && !category.trim().isEmpty()) {
            docs = documentRepository.findByUserIdAndCategoryOrderByCreatedAtDesc(userId, category);
        } else if (status != null && !status.trim().isEmpty()) {
            docs = documentRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, status);
        } else {
            docs = documentRepository.findByUserIdOrderByCreatedAtDesc(userId);
        }

        return docs.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DocumentDto getDocumentById(Long id, Long userId) {
        DocumentEntity doc = documentRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found or access denied"));
        return mapToDto(doc);
    }

    @Transactional(readOnly = true)
    public DocumentDto getPublicDocumentById(Long id, Long userId) {
        Optional<DocumentEntity> docOpt = userId != null ? documentRepository.findByIdAndUserId(id, userId) : documentRepository.findById(id);
        DocumentEntity doc = docOpt.orElseGet(() -> documentRepository.findById(id).orElse(null));
        return doc != null ? mapToDto(doc) : null;
    }

    @Transactional(readOnly = true)
    public List<ExtractedInformationDto> getDocumentExtractedInfo(Long id, Long userId) {
        DocumentEntity doc = documentRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found or access denied"));
        return extractedInformationService.getExtractedInfoByDocumentId(doc.getId());
    }

    @Transactional(readOnly = true)
    public Resource loadDocumentAsResource(Long id, Long userId) {
        DocumentEntity doc = documentRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found or access denied"));
        return loadResourceFromEntity(doc);
    }

    @Transactional(readOnly = true)
    public Resource loadPublicDocumentAsResource(Long id, Long userId) {
        Optional<DocumentEntity> docOpt = userId != null ? documentRepository.findByIdAndUserId(id, userId) : documentRepository.findById(id);
        DocumentEntity doc = docOpt.orElseGet(() -> documentRepository.findById(id).orElse(null));

        if (doc == null) {
            throw new IllegalArgumentException("Document not found");
        }
        return loadResourceFromEntity(doc);
    }

    private Resource loadResourceFromEntity(DocumentEntity doc) {
        try {
            Path filePath = Paths.get(doc.getFilePath()).normalize();
            if (!filePath.isAbsolute()) {
                filePath = this.fileStorageLocation.resolve(filePath).normalize();
            }

            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                if (!Files.exists(filePath)) {
                    Files.createDirectories(filePath.getParent());
                    String textContent = "DocMind AI Vault Encrypted Document\nFilename: " + doc.getFileName() + "\nCategory: " + doc.getCategory();
                    Files.write(filePath, textContent.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                }
                return new UrlResource(filePath.toUri());
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException("Could not read document file: " + doc.getFileName(), ex);
        }
    }

    @Transactional
    public void deleteDocument(Long id, Long userId) {
        DocumentEntity doc = documentRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Document not found or access denied"));

        // 1. Delete Foreign Key ExtractedInformation records
        extractedInformationRepository.deleteByDocumentId(doc.getId());
        extractedInformationRepository.flush();

        // 2. Unlink Foreign Key references in Skill, Education, Experience, Certification
        List<Skill> skills = skillRepository.findByUserId(userId);
        for (Skill s : skills) {
            if (s.getVerifiedByDoc() != null && s.getVerifiedByDoc().getId().equals(doc.getId())) {
                s.setVerifiedByDoc(null);
                skillRepository.save(s);
            }
        }

        List<Education> edus = educationRepository.findByUserId(userId);
        for (Education e : edus) {
            if (e.getVerifiedByDoc() != null && e.getVerifiedByDoc().getId().equals(doc.getId())) {
                e.setVerifiedByDoc(null);
                educationRepository.save(e);
            }
        }

        List<Experience> exps = experienceRepository.findByUserId(userId);
        for (Experience e : exps) {
            if (e.getVerifiedByDoc() != null && e.getVerifiedByDoc().getId().equals(doc.getId())) {
                e.setVerifiedByDoc(null);
                experienceRepository.save(e);
            }
        }

        List<Certification> certs = certificationRepository.findByUserId(userId);
        for (Certification c : certs) {
            if (c.getVerifiedByDoc() != null && c.getVerifiedByDoc().getId().equals(doc.getId())) {
                c.setVerifiedByDoc(null);
                certificationRepository.save(c);
            }
        }

        // 3. Delete file on disk
        try {
            Path filePath = Paths.get(doc.getFilePath()).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            // Ignore file deletion error
        }

        // 4. Delete document entity cleanly & flush
        documentRepository.delete(doc);
        documentRepository.flush();

        auditLogService.logAction(doc.getUser(), "DELETE", "Document: " + doc.getFileName(), 
                "Deleted document ID: " + doc.getId(), null, null);
    }

    public String autoDetectCategory(String filename) {
        String name = filename.toLowerCase();
        if (name.contains("aadhaar") || name.contains("pan") || name.contains("passport") || name.contains("identity") || name.contains("voter")) {
            return "Identity";
        } else if (name.contains("degree") || name.contains("marksheet") || name.contains("certificate") || name.contains("10th") || name.contains("12th") || name.contains("diploma")) {
            return "Education";
        } else if (name.contains("resume") || name.contains("cv") || name.contains("biodata")) {
            return "Resume";
        } else if (name.contains("license") || name.contains("licence") || name.contains("driving")) {
            return "Licenses";
        } else if (name.contains("salary") || name.contains("payslip") || name.contains("tax") || name.contains("bank")) {
            return "Financial";
        } else if (name.contains("community") || name.contains("caste") || name.contains("income") || name.contains("govt")) {
            return "Government";
        }
        return "Other";
    }

    public String autoDetectDocType(String filename) {
        String name = filename.toLowerCase();
        if (name.contains("aadhaar")) return "Aadhaar Card";
        if (name.contains("pan")) return "PAN Card";
        if (name.contains("passport")) return "Passport";
        if (name.contains("degree")) return "Degree Certificate";
        if (name.contains("10th")) return "10th Marksheet";
        if (name.contains("12th")) return "12th Marksheet";
        if (name.contains("resume") || name.contains("cv")) return "Resume";
        if (name.contains("license") || name.contains("licence") || name.contains("driving")) return "Driving Licence";
        return "General Document";
    }

    public String computeStatus(LocalDate expiryDate) {
        if (expiryDate == null) return "ACTIVE";
        LocalDate today = LocalDate.now();
        if (expiryDate.isBefore(today)) {
            return "EXPIRED";
        }
        long daysUntilExpiry = ChronoUnit.DAYS.between(today, expiryDate);
        if (daysUntilExpiry <= 30) {
            return "EXPIRING_SOON";
        }
        return "ACTIVE";
    }

    public DocumentDto mapToDto(DocumentEntity doc) {
        return DocumentDto.builder()
                .id(doc.getId())
                .fileName(doc.getFileName())
                .fileType(doc.getFileType())
                .fileSize(doc.getFileSize())
                .category(doc.getCategory())
                .documentType(doc.getDocumentType())
                .issueDate(doc.getIssueDate())
                .expiryDate(doc.getExpiryDate())
                .status(doc.getStatus())
                .isEncrypted(doc.getIsEncrypted())
                .createdAt(doc.getCreatedAt())
                .build();
    }
}
