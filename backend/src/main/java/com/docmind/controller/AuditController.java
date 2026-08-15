package com.docmind.controller;

import com.docmind.dto.ApiResponse;
import com.docmind.dto.AuditLogDto;
import com.docmind.security.UserPrincipal;
import com.docmind.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditController {

    private final AuditLogService auditLogService;

    public AuditController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AuditLogDto>>> getUserAuditLogs(@AuthenticationPrincipal UserPrincipal principal) {
        Long userId = (principal != null) ? principal.getId() : 1L;
        List<AuditLogDto> logs = auditLogService.getUserAuditLogs(userId);
        return ResponseEntity.ok(ApiResponse.success(logs));
    }
}
