package com.docmind.repository;

import com.docmind.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByUserIdOrderByReminderDateAsc(Long userId);
    List<Reminder> findByUserIdAndIsReadFalseOrderByReminderDateAsc(Long userId);
}
