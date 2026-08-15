package com.docmind.repository;

import com.docmind.entity.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {
    List<Opportunity> findByOpportunityType(String opportunityType);
    List<Opportunity> findByTitleContainingIgnoreCaseOrOrganizationContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String title, String organization, String description
    );
}
