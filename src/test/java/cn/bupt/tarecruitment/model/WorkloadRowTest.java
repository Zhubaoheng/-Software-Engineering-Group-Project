package cn.bupt.tarecruitment.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link WorkloadRow} value object covering both the
 * full constructor and the no-arg constructor with setters.
 */
@DisplayName("WorkloadRow value object")
class WorkloadRowTest {

    @Test
    @DisplayName("Full constructor stores all fields and exposes them via accessors")
    void fullConstructorStoresAllFields() {
        // Arrange & Act
        WorkloadRow row = new WorkloadRow("ta-1", "Alice", 3, 18, true);

        // Assert
        assertEquals("ta-1", row.getApplicantId());
        assertEquals("Alice", row.getApplicantName());
        assertEquals(3, row.getAssignmentCount());
        assertEquals(18, row.getTotalHours());
        assertTrue(row.isOverloaded());
    }

    @Test
    @DisplayName("No-arg constructor combined with setters populates every field")
    void settersPopulateEveryField() {
        // Arrange
        WorkloadRow row = new WorkloadRow();

        // Act
        row.setApplicantId("ta-2");
        row.setApplicantName("Bob");
        row.setAssignmentCount(1);
        row.setTotalHours(6);
        row.setOverloaded(false);

        // Assert
        assertEquals("ta-2", row.getApplicantId());
        assertEquals("Bob", row.getApplicantName());
        assertEquals(1, row.getAssignmentCount());
        assertEquals(6, row.getTotalHours());
        assertFalse(row.isOverloaded());
    }
}
