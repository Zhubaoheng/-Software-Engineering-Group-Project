package cn.bupt.tarecruitment.service.impl;

import cn.bupt.tarecruitment.model.ApplicantProfile;
import cn.bupt.tarecruitment.model.JobPost;
import cn.bupt.tarecruitment.model.SkillMatch;
import cn.bupt.tarecruitment.service.MatchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the rule-based {@link DefaultMatchService} skill-matching
 * algorithm. All fixtures are built in memory so the tests touch no files.
 */
@DisplayName("DefaultMatchService skill-matching algorithm")
class DefaultMatchServiceTest {

    private final MatchService matchService = new DefaultMatchService();

    private JobPost job(String id, String... skills) {
        JobPost jobPost = new JobPost();
        jobPost.setJobId(id);
        jobPost.setRequiredSkills(List.of(skills));
        return jobPost;
    }

    private ApplicantProfile profile(String... skills) {
        ApplicantProfile applicantProfile = new ApplicantProfile("ta-test");
        applicantProfile.setSkills(List.of(skills));
        return applicantProfile;
    }

    @Test
    @DisplayName("All required skills covered yields score 100 and STRONG level")
    void strongMatchCoversEverything() {
        // Arrange
        JobPost jobPost = job("job-1", "Java", "Git", "Teamwork");
        ApplicantProfile applicant = profile("Java", "Git", "Teamwork");

        // Act
        SkillMatch result = matchService.match(jobPost, applicant);

        // Assert
        assertEquals(100, result.getScorePercent());
        assertEquals("STRONG", result.getLevel());
        assertTrue(result.getMissingSkills().isEmpty());
        assertEquals(3, result.getMatchedSkills().size());
    }

    @Test
    @DisplayName("Partial coverage at or above 40% yields a MODERATE level")
    void moderateMatchCoversSome() {
        // Arrange: 2 of 4 required skills covered -> 50%
        JobPost jobPost = job("job-1", "Java", "Git", "Teamwork", "Python");
        ApplicantProfile applicant = profile("Java", "Git");

        // Act
        SkillMatch result = matchService.match(jobPost, applicant);

        // Assert
        assertEquals(50, result.getScorePercent());
        assertEquals("MODERATE", result.getLevel());
        assertEquals(2, result.getMatchedSkills().size());
        assertEquals(2, result.getMissingSkills().size());
    }

    @Test
    @DisplayName("Coverage below 40% yields a WEAK level")
    void weakMatchCoversLittle() {
        // Arrange: 1 of 4 required skills covered -> 25%
        JobPost jobPost = job("job-1", "Java", "Git", "Teamwork", "Python");
        ApplicantProfile applicant = profile("Java");

        // Act
        SkillMatch result = matchService.match(jobPost, applicant);

        // Assert
        assertEquals(25, result.getScorePercent());
        assertEquals("WEAK", result.getLevel());
    }

    @Test
    @DisplayName("Missing skills list reports exactly the uncovered required skills")
    void missingSkillsAreReportedAccurately() {
        // Arrange
        JobPost jobPost = job("job-1", "Java", "Git", "Teamwork");
        ApplicantProfile applicant = profile("Java");

        // Act
        SkillMatch result = matchService.match(jobPost, applicant);

        // Assert
        assertTrue(result.getMatchedSkills().contains("Java"));
        assertTrue(result.getMissingSkills().contains("Git"));
        assertTrue(result.getMissingSkills().contains("Teamwork"));
        assertEquals(2, result.getMissingSkills().size());
    }

    @Test
    @DisplayName("A job with no required skills is trivially a STRONG match at score 100")
    void jobWithNoRequiredSkillsIsFullMatch() {
        // Arrange
        JobPost jobPost = job("job-1");
        ApplicantProfile applicant = profile("Java");

        // Act
        SkillMatch result = matchService.match(jobPost, applicant);

        // Assert
        assertEquals(100, result.getScorePercent());
        assertEquals("STRONG", result.getLevel());
        assertTrue(result.getMissingSkills().isEmpty());
    }

    @Test
    @DisplayName("A null profile produces score 0 with all required skills missing")
    void nullProfileScoresZero() {
        // Arrange
        JobPost jobPost = job("job-1", "Java", "Git");

        // Act
        SkillMatch result = matchService.match(jobPost, null);

        // Assert
        assertEquals(0, result.getScorePercent());
        assertEquals("WEAK", result.getLevel());
        assertEquals(2, result.getMissingSkills().size());
    }

    @Test
    @DisplayName("An empty-skills profile produces score 0 with all required skills missing")
    void emptyProfileScoresZero() {
        // Arrange
        JobPost jobPost = job("job-1", "Java", "Git");
        ApplicantProfile applicant = profile();

        // Act
        SkillMatch result = matchService.match(jobPost, applicant);

        // Assert
        assertEquals(0, result.getScorePercent());
        assertTrue(result.getMatchedSkills().isEmpty());
        assertEquals(2, result.getMissingSkills().size());
    }

    @Test
    @DisplayName("A null job is treated as having no required skills (full match)")
    void nullJobIsFullMatch() {
        // Act
        SkillMatch result = matchService.match(null, profile("Java"));

        // Assert
        assertEquals(100, result.getScorePercent());
        assertEquals("STRONG", result.getLevel());
    }

    @Test
    @DisplayName("Skill matching is case-insensitive and ignores surrounding whitespace")
    void matchingIsCaseAndWhitespaceInsensitive() {
        // Arrange
        JobPost jobPost = job("job-1", "Java", "Git");
        ApplicantProfile applicant = profile("  java ", "GIT");

        // Act
        SkillMatch result = matchService.match(jobPost, applicant);

        // Assert
        assertEquals(100, result.getScorePercent());
    }

    @Test
    @DisplayName("Word-level rule: applicant skill 'software' covers required 'Software Engineering'")
    void wordLevelContainmentApplicantSubsetCoversRequired() {
        // Arrange
        JobPost jobPost = job("job-1", "Software Engineering");
        ApplicantProfile applicant = profile("software");

        // Act
        SkillMatch result = matchService.match(jobPost, applicant);

        // Assert
        assertEquals(100, result.getScorePercent());
        assertTrue(result.getMatchedSkills().contains("Software Engineering"));
    }

    @Test
    @DisplayName("Word-level rule: applicant skill 'Software Engineering' covers required 'Software'")
    void wordLevelContainmentRequiredSubsetCoveredByApplicant() {
        // Arrange
        JobPost jobPost = job("job-1", "Software");
        ApplicantProfile applicant = profile("Software Engineering");

        // Act
        SkillMatch result = matchService.match(jobPost, applicant);

        // Assert
        assertEquals(100, result.getScorePercent());
    }

    @Test
    @DisplayName("Word-level rule does not match merely overlapping skill phrases")
    void wordLevelRuleDoesNotMatchPartialOverlap() {
        // Arrange: "Java" and "Python" share no words, neither contains the other
        JobPost jobPost = job("job-1", "Advanced Java Programming");
        ApplicantProfile applicant = profile("Python Programming");

        // Act
        SkillMatch result = matchService.match(jobPost, applicant);

        // Assert
        assertEquals(0, result.getScorePercent());
        assertTrue(result.getMissingSkills().contains("Advanced Java Programming"));
    }

    @Test
    @DisplayName("matchJobsForApplicant returns a map keyed by job id")
    void matchJobsForApplicantKeyedByJobId() {
        // Arrange
        JobPost a = job("job-001", "Java");
        JobPost b = job("job-002", "Git");
        ApplicantProfile applicant = profile("Java");

        // Act
        Map<String, SkillMatch> results = matchService.matchJobsForApplicant(List.of(a, b), applicant);

        // Assert
        assertEquals(2, results.size());
        assertTrue(results.containsKey("job-001"));
        assertTrue(results.containsKey("job-002"));
        assertEquals(100, results.get("job-001").getScorePercent());
        assertEquals(0, results.get("job-002").getScorePercent());
    }

    @Test
    @DisplayName("matchJobsForApplicant returns an empty map when the job list is null")
    void matchJobsForApplicantHandlesNullList() {
        // Act
        Map<String, SkillMatch> results = matchService.matchJobsForApplicant(null, profile("Java"));

        // Assert
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("matchJobsForApplicant skips jobs with a null id")
    void matchJobsForApplicantSkipsJobsWithoutId() {
        // Arrange
        JobPost withId = job("job-001", "Java");
        JobPost withoutId = job(null, "Git");

        // Act
        Map<String, SkillMatch> results = matchService.matchJobsForApplicant(
                java.util.Arrays.asList(withId, withoutId), profile("Java"));

        // Assert
        assertEquals(1, results.size());
        assertTrue(results.containsKey("job-001"));
    }

    @Test
    @DisplayName("Score rounds to the nearest whole percent")
    void scoreRoundsToNearestPercent() {
        // Arrange: 2 of 3 covered -> 66.67% rounds to 67
        JobPost jobPost = job("job-1", "Java", "Git", "Python");
        ApplicantProfile applicant = profile("Java", "Git");

        // Act
        SkillMatch result = matchService.match(jobPost, applicant);

        // Assert
        assertEquals(67, result.getScorePercent());
        assertEquals("MODERATE", result.getLevel());
    }

    @Test
    @DisplayName("Explanation text names the missing skills for a partial match")
    void explanationMentionsMissingSkills() {
        // Arrange
        JobPost jobPost = job("job-1", "Java", "Git");
        ApplicantProfile applicant = profile("Java");

        // Act
        SkillMatch result = matchService.match(jobPost, applicant);

        // Assert
        assertTrue(result.getExplanation().contains("Git"));
        assertFalse(result.getExplanation().isBlank());
    }
}
