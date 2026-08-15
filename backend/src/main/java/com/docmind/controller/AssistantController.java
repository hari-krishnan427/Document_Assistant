package com.docmind.controller;

import com.docmind.dto.ApiResponse;
import com.docmind.dto.AssistantChatRequestDto;
import com.docmind.dto.AssistantChatResponseDto;
import com.docmind.security.UserPrincipal;
import com.docmind.service.AssistantService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assistant")
public class AssistantController {

    private final AssistantService assistantService;

    public AssistantController(AssistantService assistantService) {
        this.assistantService = assistantService;
    }

    @PostMapping("/chat")
    public ResponseEntity<ApiResponse<AssistantChatResponseDto>> chat(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody AssistantChatRequestDto request) {
        
        Long userId = (principal != null) ? principal.getId() : 1L;
        AssistantChatResponseDto response = assistantService.processChat(userId, request.getQuery());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
