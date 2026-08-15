package com.docmind.repository;

import com.docmind.entity.DocumentBundle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentBundleRepository extends JpaRepository<DocumentBundle, Long> {
    List<DocumentBundle> findByUserIdOrderByCreatedAtDesc(Long userId);
}
