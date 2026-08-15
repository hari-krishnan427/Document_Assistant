package com.docmind.service;

import com.docmind.dto.OpportunityDto;
import com.docmind.entity.*;
import com.docmind.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OpportunityService {

    private final OpportunityRepository opportunityRepository;
    private final SkillRepository skillRepository;
    private final EducationRepository educationRepository;
    private final DocumentRepository documentRepository;
    private final RestTemplate restTemplate;

    @Value("${app.ai-service.url:http://localhost:8000}")
    private String aiServiceUrl;

    public OpportunityService(OpportunityRepository opportunityRepository,
                               SkillRepository skillRepository,
                               EducationRepository educationRepository,
                               DocumentRepository documentRepository) {
        this.opportunityRepository = opportunityRepository;
        this.skillRepository = skillRepository;
        this.educationRepository = educationRepository;
        this.documentRepository = documentRepository;
        this.restTemplate = new RestTemplate();
    }

    @Transactional
    public List<OpportunityDto> getOpportunities(Long userId, String query, String type, String location, int page, int pageSize) {
        List<Opportunity> dbList = opportunityRepository.findAll();

        List<String> userSkills = skillRepository.findByUserId(userId).stream()
                .map(s -> s.getSkillName().toLowerCase())
                .collect(Collectors.toList());

        List<DocumentEntity> userDocs = documentRepository.findByUserIdOrderByCreatedAtDesc(userId);

        Map<Long, OpportunityDto> dtoMap = new LinkedHashMap<>();

        // 1. Process database entries for page 1
        if (page == 1) {
            for (Opportunity opp : dbList) {
                if (type != null && !type.equalsIgnoreCase("ALL") && !opp.getOpportunityType().equalsIgnoreCase(type)) {
                    continue;
                }
                if (query != null && !query.trim().isEmpty()) {
                    String q = query.toLowerCase();
                    boolean matches = opp.getTitle().toLowerCase().contains(q) ||
                                     opp.getOrganization().toLowerCase().contains(q) ||
                                     opp.getDescription().toLowerCase().contains(q) ||
                                     opp.getLocation().toLowerCase().contains(q);
                    if (!matches) continue;
                }
                OpportunityDto dto = calculateMatchForOpportunity(opp.getId(), opp.getTitle(), opp.getOrganization(), 
                        opp.getOpportunityType(), opp.getDescription(), opp.getLocation(), opp.getSalaryOrStipend(), 
                        opp.getDeadline(), opp.getOfficialUrl(), opp.getSource(), opp.getPublishedDate(), userSkills, userDocs);
                dtoMap.put(dto.getId(), dto);
            }
        }

        // 2. Fetch live market entries from Python AI microservice for exact page number
        List<OpportunityDto> liveDtos = fetchLiveJobsFromAiService(query, location, type, page, pageSize, userSkills, userDocs);
        for (OpportunityDto live : liveDtos) {
            if (!dtoMap.containsKey(live.getId())) {
                dtoMap.put(live.getId(), live);
            }
        }

        // 3. PRIORITY SORTING: Highest Match Score ALWAYS comes at the VERY TOP of every page!
        List<OpportunityDto> result = new ArrayList<>(dtoMap.values());
        result.sort((a, b) -> Integer.compare(b.getMatchScore(), a.getMatchScore()));
        return result;
    }

    public List<OpportunityDto> getOpportunities(Long userId, String query, String type, String location) {
        return getOpportunities(userId, query, type, location, 1, 10);
    }

    private List<OpportunityDto> fetchLiveJobsFromAiService(String query, String location, String type, int page, int pageSize, List<String> userSkills, List<DocumentEntity> userDocs) {
        List<OpportunityDto> liveList = new ArrayList<>();
        try {
            String baseUrl = (aiServiceUrl.startsWith("http://") || aiServiceUrl.startsWith("https://")) ? aiServiceUrl : "http://" + aiServiceUrl;
            String liveJobsUrl = baseUrl + "/api/ai/live-jobs";
            String skillsParam = String.join(",", userSkills);
            String uri = UriComponentsBuilder.fromHttpUrl(liveJobsUrl)
                    .queryParam("query", query != null ? query : "")
                    .queryParam("location", location != null ? location : "")
                    .queryParam("type", type != null ? type : "")
                    .queryParam("page", page)
                    .queryParam("page_size", pageSize)
                    .queryParam("skills", skillsParam)
                    .build()
                    .toUriString();

            Map resp = restTemplate.getForObject(uri, Map.class);
            if (resp != null && resp.containsKey("jobs")) {
                List<Map<String, Object>> jobs = (List<Map<String, Object>>) resp.get("jobs");
                for (Map<String, Object> job : jobs) {
                    Long id = 1000L;
                    Object idObj = job.get("id");
                    if (idObj instanceof Number) {
                        id = ((Number) idObj).longValue();
                    } else if (idObj != null) {
                        id = (long) Math.abs(idObj.toString().hashCode());
                    }

                    String title = (String) job.get("title");
                    String org = (String) job.get("organization");
                    String oppType = (String) job.get("opportunityType");
                    String desc = (String) job.get("description");
                    String loc = (String) job.get("location");
                    String salary = (String) job.get("salaryOrStipend");
                    String officialUrl = (String) job.get("officialUrl");
                    String source = (String) job.get("source");

                    OpportunityDto dto = calculateMatchForOpportunity(id, title, org, oppType, desc, loc, salary, 
                            LocalDate.now().plusDays(30), officialUrl, source, LocalDateTime.now(), userSkills, userDocs);
                    liveList.add(dto);
                }
            }
        } catch (Exception e) {
            // Log connection failure gracefully
        }
        return liveList;
    }

    private OpportunityDto calculateMatchForOpportunity(
            Long id, String title, String org, String type, String desc, String location,
            String salary, LocalDate deadline, String officialUrl, String source, LocalDateTime publishedDate,
            List<String> userSkills, List<DocumentEntity> userDocs) {

        if (userDocs.isEmpty()) {
            return OpportunityDto.builder()
                    .id(id)
                    .title(title)
                    .organization(org)
                    .opportunityType(type)
                    .description(desc)
                    .location(location)
                    .salaryOrStipend(salary)
                    .deadline(deadline)
                    .officialUrl(officialUrl)
                    .source(source)
                    .publishedDate(publishedDate)
                    .matchScore(40)
                    .eligibilityStatus("NOT_ELIGIBLE")
                    .matchedSkills("No verified documents uploaded in vault")
                    .missingSkills("Degree Certificate / Resume required")
                    .missingDocuments("Degree Certificate (Missing), Updated Resume (Missing), Identity Proof (Missing)")
                    .aiRecommendation("Low Match (40%). Upload your verified Degree Certificate and Resume to unlock full match eligibility.")
                    .build();
        }

        String combinedText = ((title != null ? title : "") + " " + (desc != null ? desc : "") + " " + (org != null ? org : "")).toLowerCase();

        Set<String> matchedSet = new LinkedHashSet<>();
        Set<String> missingSet = new LinkedHashSet<>();

        for (String skill : userSkills) {
            if (combinedText.contains(skill.toLowerCase())) {
                matchedSet.add(capitalize(skill));
            }
        }

        int score = 50; // Base alignment score for having verified docs
        if (!matchedSet.isEmpty()) {
            score = Math.min(98, 70 + (matchedSet.size() * 8));
        }

        String matchedStr = matchedSet.isEmpty() ? "Verified Educational Credentials" : String.join(", ", matchedSet);
        String missingStr = missingSet.isEmpty() ? "None" : String.join(", ", missingSet);
        String eligibility = score >= 80 ? "ELIGIBLE" : (score >= 60 ? "PARTIALLY_ELIGIBLE" : "NOT_ELIGIBLE");

        String aiRec = String.format("Match Score: %d%% (%s). %s.", score, eligibility.replace("_", " "),
                matchedSet.isEmpty() ? "Profile aligns with general eligibility." : "Matched skills: " + matchedStr);

        return OpportunityDto.builder()
                .id(id)
                .title(title)
                .organization(org)
                .opportunityType(type)
                .description(desc)
                .location(location)
                .salaryOrStipend(salary)
                .deadline(deadline)
                .officialUrl(officialUrl)
                .source(source)
                .publishedDate(publishedDate)
                .matchScore(score)
                .eligibilityStatus(eligibility)
                .matchedSkills(matchedStr)
                .missingSkills(missingStr)
                .missingDocuments("Verified in Vault")
                .aiRecommendation(aiRec)
                .build();
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
