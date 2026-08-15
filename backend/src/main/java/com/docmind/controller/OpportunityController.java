package com.docmind.controller;

import com.docmind.dto.ApiResponse;
import com.docmind.dto.OpportunityDto;
import com.docmind.security.UserPrincipal;
import com.docmind.service.OpportunityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/opportunities")
public class OpportunityController {

    private final OpportunityService opportunityService;

    public OpportunityController(OpportunityService opportunityService) {
        this.opportunityService = opportunityService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OpportunityDto>>> getOpportunities(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        
        Long userId = (principal != null) ? principal.getId() : 1L;
        List<OpportunityDto> opportunities = opportunityService.getOpportunities(userId, query, type, location, page, pageSize);
        return ResponseEntity.ok(ApiResponse.success(opportunities));
    }

    @GetMapping("/top-matches")
    public ResponseEntity<ApiResponse<List<OpportunityDto>>> getTopMatches(@AuthenticationPrincipal UserPrincipal principal) {
        Long userId = (principal != null) ? principal.getId() : 1L;
        List<OpportunityDto> opportunities = opportunityService.getOpportunities(userId, null, "ALL", "ALL", 1, 10);
        List<OpportunityDto> topMatches = opportunities.stream().limit(3).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(topMatches));
    }
}
