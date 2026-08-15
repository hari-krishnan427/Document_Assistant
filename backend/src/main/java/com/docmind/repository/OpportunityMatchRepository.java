package com.docmind.repository;

import com.docmind.entity.OpportunityMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OpportunityMatchRepository extends JpaRepository<OpportunityMatch, Long> {
    List<OpportunityMatch> findByUserIdOrderByMatchScoreDesc(Long userId);
    Optional<OpportunityMatch> findByUserIdAndOpportunityId(Long userId, Long opportunityId);
}
