package cn.bupt.tarecruitment.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for the {@link WorkloadRecommendation} value object covering
 * both the no-arg and the full constructors.
 */
@DisplayName("WorkloadRecommendation value object")
class WorkloadRecommendationTest {

    @Test
    @DisplayName("Full constructor stores all fields and exposes them via accessors")
    void fullConstructorStoresAllFields() {
        // Arrange & Act
        WorkloadRecommendation rec = new WorkloadRecommendation(
                "Alice", "Bob", "CS101 Intro to Programming", 6, "Alice is overloaded.");

        // Assert
        assertEquals("Alice", rec.getFromApplicantName());
        assertEquals("Bob", rec.getToApplicantName());
        assertEquals("CS101 Intro to Programming", rec.getJobLabel());
        assertEquals(6, rec.getHours());
        assertEquals("Alice is overloaded.", rec.getReason());
    }

    @Test
    @DisplayName("No-arg constructor produces an empty recommendation")
    void noArgConstructorProducesEmptyRecommendation() {
        // Act
        WorkloadRecommendation rec = new WorkloadRecommendation();

        // Assert
        assertNull(rec.getFromApplicantName());
        assertNull(rec.getToApplicantName());
        assertNull(rec.getJobLabel());
        assertEquals(0, rec.getHours());
        assertNull(rec.getReason());
    }
}
