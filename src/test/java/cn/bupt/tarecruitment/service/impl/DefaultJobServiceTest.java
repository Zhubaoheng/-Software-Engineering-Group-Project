package cn.bupt.tarecruitment.service.impl;

import cn.bupt.tarecruitment.model.JobPost;
import cn.bupt.tarecruitment.repository.JobRepository;
import cn.bupt.tarecruitment.service.JobService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Read-only behaviour tests for {@link DefaultJobService} against the
 * committed seed data in {@code data/jobs.json}. All operations exercised
 * here are queries, so the seed data is never modified.
 */
@DisplayName("DefaultJobService queries against seed data")
class DefaultJobServiceTest {

    private final JobService jobService = new DefaultJobService(new JobRepository());

    @Test
    @DisplayName("listOpenJobs returns the two seeded OPEN jobs")
    void listOpenJobsReturnsOpenJobs() {
        // Act
        List<JobPost> openJobs = jobService.listOpenJobs();

        // Assert
        assertEquals(2, openJobs.size());
        List<String> ids = openJobs.stream().map(JobPost::getJobId).toList();
        assertTrue(ids.contains("job-001"));
        assertTrue(ids.contains("job-002"));
    }

    @Test
    @DisplayName("listOpenJobs excludes the seeded CLOSED job")
    void listOpenJobsExcludesClosedJobs() {
        // Act
        List<JobPost> openJobs = jobService.listOpenJobs();

        // Assert
        List<String> ids = openJobs.stream().map(JobPost::getJobId).toList();
        assertFalse(ids.contains("job-003"));
        openJobs.forEach(job -> assertEquals("OPEN", job.getStatus().toUpperCase()));
    }

    @Test
    @DisplayName("findById resolves an existing job by its id")
    void findByIdResolvesExistingJob() {
        // Act
        Optional<JobPost> result = jobService.findById("job-001");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("CS101", result.get().getModuleCode());
    }

    @Test
    @DisplayName("findById can resolve the closed job (status query, not a filter)")
    void findByIdResolvesClosedJob() {
        // Act
        Optional<JobPost> result = jobService.findById("job-003");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("CLOSED", result.get().getStatus());
    }

    @Test
    @DisplayName("findById returns empty for an unknown job id")
    void findByIdReturnsEmptyForUnknownId() {
        // Act & Assert
        assertFalse(jobService.findById("job-does-not-exist").isPresent());
    }

    @Test
    @DisplayName("mapById indexes every seeded job, including the closed one, by id")
    void mapByIdIndexesAllJobs() {
        // Act
        Map<String, JobPost> byId = jobService.mapById();

        // Assert
        assertTrue(byId.containsKey("job-001"));
        assertTrue(byId.containsKey("job-002"));
        assertTrue(byId.containsKey("job-003"));
        assertEquals("job-002", byId.get("job-002").getJobId());
    }
}
