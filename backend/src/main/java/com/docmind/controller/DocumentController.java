package com.docmind.controller;

import com.docmind.dto.ApiResponse;
import com.docmind.dto.DocumentDto;
import com.docmind.dto.ExtractedInformationDto;
import com.docmind.entity.User;
import com.docmind.repository.UserRepository;
import com.docmind.security.UserPrincipal;
import com.docmind.service.DocumentService;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;
    private final UserRepository userRepository;

    public DocumentController(DocumentService documentService, UserRepository userRepository) {
        this.documentService = documentService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DocumentDto>>> getUserDocuments(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        List<DocumentDto> documents = documentService.getUserDocuments(principal.getId(), category, status);
        return ResponseEntity.ok(ApiResponse.success(documents));
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<DocumentDto>> uploadDocument(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "documentType", required = false) String documentType,
            @RequestParam(value = "issueDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate issueDate,
            @RequestParam(value = "expiryDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryDate) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        try {
            User user = userRepository.findById(principal.getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            DocumentDto document = documentService.uploadDocument(user, file, category, documentType, issueDate, expiryDate);
            return ResponseEntity.ok(ApiResponse.success("Document uploaded successfully", document));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Document upload failed: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentDto>> getDocumentDetails(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        try {
            DocumentDto doc = documentService.getDocumentById(id, principal.getId());
            return ResponseEntity.ok(ApiResponse.success(doc));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}/extracted-info")
    public ResponseEntity<ApiResponse<List<ExtractedInformationDto>>> getExtractedInfo(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        try {
            List<ExtractedInformationDto> info = documentService.getDocumentExtractedInfo(id, principal.getId());
            return ResponseEntity.ok(ApiResponse.success(info));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadDocument(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {

        Long userId = principal != null ? principal.getId() : null;
        DocumentDto docDto = documentService.getPublicDocumentById(id, userId);
        Resource resource = documentService.loadPublicDocumentAsResource(id, userId);

        String contentType = docDto != null ? docDto.getFileType() : "application/pdf";
        if (contentType == null || contentType.isEmpty()) {
            contentType = "application/pdf";
        }
        String fileName = docDto != null ? docDto.getFileName() : "document.pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteDocument(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        try {
            documentService.deleteDocument(id, principal.getId());
            return ResponseEntity.ok(ApiResponse.success("Document deleted successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
