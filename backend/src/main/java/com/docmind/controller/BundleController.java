package com.docmind.controller;

import com.docmind.dto.ApiResponse;
import com.docmind.dto.DocumentBundleDto;
import com.docmind.security.UserPrincipal;
import com.docmind.service.BundleService;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bundles")
public class BundleController {

    private final BundleService bundleService;

    public BundleController(BundleService bundleService) {
        this.bundleService = bundleService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DocumentBundleDto>>> getUserBundles(@AuthenticationPrincipal UserPrincipal principal) {
        Long userId = (principal != null) ? principal.getId() : 1L;
        List<DocumentBundleDto> bundles = bundleService.getUserBundles(userId);
        return ResponseEntity.ok(ApiResponse.success(bundles));
    }

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<DocumentBundleDto>> createBundle(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody Map<String, Object> req) {
        
        Long userId = (principal != null) ? principal.getId() : 1L;
        Long opportunityId = req.get("opportunityId") != null ? Long.valueOf(req.get("opportunityId").toString()) : null;
        String bundleName = (String) req.get("bundleName");
        List<Long> documentIds = (List<Long>) req.get("documentIds");

        DocumentBundleDto bundle = bundleService.createBundle(userId, opportunityId, bundleName, documentIds);
        return ResponseEntity.ok(ApiResponse.success("ZIP Document Bundle generated successfully", bundle));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadBundle(@PathVariable Long id) {
        Resource resource = bundleService.getBundleResource(id);
        if (resource == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"docmind_bundle_" + id + ".zip\"")
                .body(resource);
    }
}
