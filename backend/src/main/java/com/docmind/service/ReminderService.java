package com.docmind.service;

import com.docmind.dto.OpportunityDto;
import com.docmind.dto.ReminderDto;
import com.docmind.entity.*;
import com.docmind.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final OpportunityService opportunityService;

    public ReminderService(ReminderRepository reminderRepository,
                            DocumentRepository documentRepository,
                            UserRepository userRepository,
                            OpportunityService opportunityService) {
        this.reminderRepository = reminderRepository;
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        this.opportunityService = opportunityService;
    }

    @Transactional
    public List<ReminderDto> getReminders(Long userId) {
        List<Reminder> reminders = reminderRepository.findByUserIdOrderByReminderDateAsc(userId);

        if (reminders.isEmpty()) {
            reminders = generateRealTimeReminders(userId);
        }

        return reminders.stream().map(this::mapDto).collect(Collectors.toList());
    }

    @Transactional
    public List<Reminder> generateRealTimeReminders(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Reminder> list = new ArrayList<>();
        List<DocumentEntity> userDocs = documentRepository.findByUserIdOrderByCreatedAtDesc(userId);

        // 1. Check Document Vault Expiration Dates
        for (DocumentEntity doc : userDocs) {
            if (doc.getExpiryDate() != null) {
                long daysToExpiry = ChronoUnit.DAYS.between(LocalDate.now(), doc.getExpiryDate());
                if (daysToExpiry <= 60 && daysToExpiry >= 0) {
                    list.add(Reminder.builder()
                            .user(user)
                            .document(doc)
                            .title(doc.getFileName() + " Expiring Soon")
                            .message("Your " + doc.getFileName() + " (" + doc.getCategory() + ") expires on " + doc.getExpiryDate() + ". Please submit a renewal prior to expiry.")
                            .reminderDate(doc.getExpiryDate())
                            .priority(daysToExpiry <= 30 ? "URGENT" : "HIGH")
                            .isRead(false)
                            .build());
                }
            }
        }

        // 2. Check Real-World Opportunity Matches against Vault Documents
        List<OpportunityDto> opps = opportunityService.getOpportunities(userId, null, "ALL", "ALL");
        for (OpportunityDto opp : opps) {
            if (opp.getMatchScore() >= 80) {
                list.add(Reminder.builder()
                        .user(user)
                        .title("High Match Opportunity Alert: " + opp.getTitle())
                        .message("Live hiring notice from " + opp.getOrganization() + " matches your vault documents (" + opp.getMatchScore() + "% Match). Apply before " + opp.getDeadline() + ".")
                        .reminderDate(opp.getDeadline())
                        .priority("HIGH")
                        .isRead(false)
                        .build());
                if (list.size() >= 3) break;
            }
        }

        // 3. Vault Setup Prompt if 0 documents exist
        if (userDocs.isEmpty() && list.isEmpty()) {
            list.add(Reminder.builder()
                    .user(user)
                    .title("Vault Document Upload Recommended")
                    .message("Upload your Degree Certificate, Resume, or Identity Proof to unlock real-time hiring alerts matched against your profile.")
                    .reminderDate(LocalDate.now().plusDays(7))
                    .priority("MEDIUM")
                    .isRead(false)
                    .build());
        }

        return reminderRepository.saveAll(list);
    }

    @Transactional
    public void markAsRead(Long reminderId) {
        Reminder reminder = reminderRepository.findById(reminderId).orElse(null);
        if (reminder != null) {
            reminder.setIsRead(true);
            reminderRepository.save(reminder);
        }
    }

    private ReminderDto mapDto(Reminder r) {
        return ReminderDto.builder()
                .id(r.getId())
                .userId(r.getUser().getId())
                .documentId(r.getDocument() != null ? r.getDocument().getId() : null)
                .documentName(r.getDocument() != null ? r.getDocument().getFileName() : null)
                .title(r.getTitle())
                .message(r.getMessage())
                .reminderDate(r.getReminderDate())
                .priority(r.getPriority())
                .isRead(r.getIsRead())
                .createdAt(r.getCreatedAt())
                .build();
    }
}
