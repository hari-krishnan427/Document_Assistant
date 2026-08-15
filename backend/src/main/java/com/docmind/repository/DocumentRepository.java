package com.docmind.repository;

import com.docmind.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
    List<DocumentEntity> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<DocumentEntity> findByUserIdAndCategoryOrderByCreatedAtDesc(Long userId, String category);
    List<DocumentEntity> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, String status);
    Optional<DocumentEntity> findByIdAndUserId(Long id, Long userId);
}
