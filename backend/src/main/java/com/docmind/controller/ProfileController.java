package com.docmind.controller;

import com.docmind.dto.ApiResponse;
import com.docmind.dto.UserProfileDto;
import com.docmind.security.UserPrincipal;
import com.docmind.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<UserProfileDto>> getUserProfile(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        UserProfileDto profile = profileService.getUserProfile(principal.getId());
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<UserProfileDto>> updateUserProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody UserProfileDto dto) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        UserProfileDto updated = profileService.updateUserProfile(principal.getId(), dto);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", updated));
    }

    @PostMapping("/sync")
    public ResponseEntity<ApiResponse<UserProfileDto>> syncProfileFromDocuments(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        UserProfileDto synced = profileService.syncProfileFromDocuments(principal.getId());
        return ResponseEntity.ok(ApiResponse.success("Digital Profile auto-synced from document vault", synced));
    }
}
