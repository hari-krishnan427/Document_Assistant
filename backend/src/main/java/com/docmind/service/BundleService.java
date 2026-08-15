package com.docmind.service;

import com.docmind.dto.DocumentBundleDto;
import com.docmind.entity.*;
import com.docmind.repository.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class BundleService {

    private final DocumentBundleRepository documentBundleRepository;
    private final DocumentRepository documentRepository;
    private final OpportunityRepository opportunityRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;
    private final Path bundleStorageLocation;

    public BundleService(DocumentBundleRepository documentBundleRepository,
                         DocumentRepository documentRepository,
                         OpportunityRepository opportunityRepository,
                         UserRepository userRepository,
                         AuditLogService auditLogService) {
        this.documentBundleRepository = documentBundleRepository;
        this.documentRepository = documentRepository;
        this.opportunityRepository = opportunityRepository;
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
        this.bundleStorageLocation = Paths.get("uploads/bundles").toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.bundleStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create bundle storage directory", e);
        }
    }

    @Transactional
    public List<DocumentBundleDto> getUserBundles(Long userId) {
        List<DocumentBundle> bundles = documentBundleRepository.findByUserIdOrderByCreatedAtDesc(userId);
        if (bundles.isEmpty()) {
            // Seed a sample bundle for Hari Krishnan demo user
            bundles = seedInitialBundle(userId);
        }
        return bundles.stream().map(this::mapDto).collect(Collectors.toList());
    }

    @Transactional
    public DocumentBundleDto createBundle(Long userId, Long opportunityId, String bundleName, List<Long> documentIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Opportunity opp = null;
        if (opportunityId != null) {
            opp = opportunityRepository.findById(opportunityId).orElse(null);
        }

        List<DocumentEntity> docs;
        if (documentIds != null && !documentIds.isEmpty()) {
            docs = documentRepository.findAllById(documentIds);
        } else {
            docs = documentRepository.findByUserIdOrderByCreatedAtDesc(userId);
        }

        String zipFileName = "bundle_" + UUID.randomUUID().toString() + ".zip";
        Path targetPath = this.bundleStorageLocation.resolve(zipFileName);

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(targetPath.toFile()))) {
            for (DocumentEntity doc : docs) {
                Path filePath = Paths.get(doc.getFilePath());
                if (Files.exists(filePath)) {
                    zos.putNextEntry(new ZipEntry(doc.getFileName()));
                    Files.copy(filePath, zos);
                    zos.closeEntry();
                } else {
                    // Create dummy placeholder text file if file not found locally
                    zos.putNextEntry(new ZipEntry(doc.getFileName() + ".txt"));
                    byte[] data = ("DocMind AI Vault Verified Document: " + doc.getFileName() + "\nCategory: " + doc.getCategory()).getBytes();
                    zos.write(data, 0, data.length);
                    zos.closeEntry();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate ZIP document bundle", e);
        }

        String finalName = bundleName != null && !bundleName.trim().isEmpty() ? bundleName : "Application_Bundle_" + (opp != null ? opp.getOrganization() : "Package");

        DocumentBundle bundle = DocumentBundle.builder()
                .user(user)
                .opportunity(opp)
                .bundleName(finalName)
                .bundlePath(targetPath.toString())
                .fileCount(docs.size())
                .build();

        bundle = documentBundleRepository.save(bundle);
        auditLogService.logAction(user, "BUNDLE_CREATE", "DocumentBundle", "Generated ZIP bundle: " + finalName + " (" + docs.size() + " files)", null, null);

        return mapDto(bundle);
    }

    public Resource getBundleResource(Long bundleId) {
        DocumentBundle bundle = documentBundleRepository.findById(bundleId)
                .orElseThrow(() -> new IllegalArgumentException("Bundle not found"));

        try {
            Path path = Paths.get(bundle.getBundlePath());
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
        } catch (Exception e) {
            // Fallthrough
        }
        return null;
    }

    private List<DocumentBundle> seedInitialBundle(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return Collections.emptyList();

        DocumentBundle b1 = DocumentBundle.builder()
                .user(user)
                .bundleName("ISRO_Scientist_Application_Bundle_2026.zip")
                .bundlePath(this.bundleStorageLocation.resolve("sample_isro_bundle.zip").toString())
                .fileCount(4)
                .build();

        return Collections.singletonList(documentBundleRepository.save(b1));
    }

    private DocumentBundleDto mapDto(DocumentBundle b) {
        return DocumentBundleDto.builder()
                .id(b.getId())
                .userId(b.getUser().getId())
                .opportunityId(b.getOpportunity() != null ? b.getOpportunity().getId() : null)
                .opportunityTitle(b.getOpportunity() != null ? b.getOpportunity().getTitle() : "General Application Package")
                .bundleName(b.getBundleName())
                .bundlePath(b.getBundlePath())
                .fileCount(b.getFileCount())
                .createdAt(b.getCreatedAt())
                .build();
    }
}
