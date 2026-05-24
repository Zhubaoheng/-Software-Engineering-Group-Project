package cn.bupt.tarecruitment.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link SkillMatch} value object: construction,
 * accessors, and the defensive handling of null skill lists.
 */
@DisplayName("SkillMatch value object")
class SkillMatchTest {

    @Test
    @DisplayName("Constructor stores all fields and exposes them via accessors")
    void constructorStoresAllFields() {
        // Arrange & Act
        SkillMatch match = new SkillMatch(80,
                List.of("Java", "Git"),
                List.of("Python"),
                "STRONG",
                "Strong match");

        // Assert
        assertEquals(80, match.getScorePercent());
        assertEquals(List.of("Java", "Git"), match.getMatchedSkills());
        assertEquals(List.of("Python"), match.getMissingSkills());
        assertEquals("STRONG", match.getLevel());
        assertEquals("Strong match", match.getExplanation());
    }

    @Test
    @DisplayName("Null skill lists are normalised to empty lists")
    void nullSkillListsBecomeEmpty() {
        // Act
        SkillMatch match = new SkillMatch(0, null, null, "WEAK", "");

        // Assert
        assertTrue(match.getMatchedSkills().isEmpty());
        assertTrue(match.getMissingSkills().isEmpty());
    }

    @Test
    @DisplayName("hasMatchedSkills reflects whether any skill matched")
    void hasMatchedSkillsReflectsContent() {
        // Assert
        assertTrue(new SkillMatch(50, List.of("Java"), List.of(), "MODERATE", "").hasMatchedSkills());
        assertFalse(new SkillMatch(0, List.of(), List.of("Java"), "WEAK", "").hasMatchedSkills());
    }

    @Test
    @DisplayName("hasMissingSkills reflects whether any required skill is missing")
    void hasMissingSkillsReflectsContent() {
        // Assert
        assertTrue(new SkillMatch(50, List.of("Java"), List.of("Git"), "MODERATE", "").hasMissingSkills());
        assertFalse(new SkillMatch(100, List.of("Java"), List.of(), "STRONG", "").hasMissingSkills());
    }

    @Test
    @DisplayName("Stored skill lists are defensive copies of the constructor input")
    void skillListsAreDefensiveCopies() {
        // Arrange
        List<String> source = new java.util.ArrayList<>(List.of("Java"));
        SkillMatch match = new SkillMatch(100, source, List.of(), "STRONG", "");

        // Act: mutate the original source list
        source.add("Git");

        // Assert: the SkillMatch is unaffected
        assertEquals(1, match.getMatchedSkills().size());
    }
}
