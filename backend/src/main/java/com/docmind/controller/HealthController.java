package com.docmind.controller;

import com.docmind.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, String>>> checkHealth() {
        Map<String, String> status = Map.of(
                "status", "UP",
                "service", "DocMind AI Backend",
                "version", "1.0.0"
        );
        return ResponseEntity.ok(ApiResponse.success("System operational", status));
    }
}
