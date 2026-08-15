package com.docmind.repository;

import com.docmind.entity.ExtractedInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtractedInformationRepository extends JpaRepository<ExtractedInformation, Long> {
    List<ExtractedInformation> findByDocumentId(Long documentId);
    void deleteByDocumentId(Long documentId);
}
