package com.docmind.controller;

import com.docmind.dto.ApiResponse;
import com.docmind.dto.ReminderDto;
import com.docmind.security.UserPrincipal;
import com.docmind.service.ReminderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReminderDto>>> getReminders(@AuthenticationPrincipal UserPrincipal principal) {
        Long userId = (principal != null) ? principal.getId() : 1L;
        List<ReminderDto> reminders = reminderService.getReminders(userId);
        return ResponseEntity.ok(ApiResponse.success(reminders));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable Long id) {
        reminderService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.success("Reminder marked as read", null));
    }
}
