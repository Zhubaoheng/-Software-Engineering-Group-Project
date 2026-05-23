package cn.bupt.tarecruitment.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the pure string helpers in {@link WebUtils}
 * ({@code splitCsv}, {@code joinCsv}, {@code escapeHtml}).
 */
@DisplayName("WebUtils string helpers")
class WebUtilsTest {

    @Test
    @DisplayName("splitCsv splits a comma-separated string and trims each value")
    void splitCsvSplitsOnCommas() {
        // Act
        List<String> result = WebUtils.splitCsv("Java, Git , Teamwork");

        // Assert
        assertEquals(List.of("Java", "Git", "Teamwork"), result);
    }

    @Test
    @DisplayName("splitCsv also splits on newlines")
    void splitCsvSplitsOnNewlines() {
        // Act
        List<String> result = WebUtils.splitCsv("Java\nGit\nTeamwork");

        // Assert
        assertEquals(List.of("Java", "Git", "Teamwork"), result);
    }

    @Test
    @DisplayName("splitCsv handles mixed comma and newline separators")
    void splitCsvSplitsOnMixedSeparators() {
        // Act
        List<String> result = WebUtils.splitCsv("Java,\nGit\n, Teamwork");

        // Assert
        assertEquals(List.of("Java", "Git", "Teamwork"), result);
    }

    @Test
    @DisplayName("splitCsv drops blank entries between separators")
    void splitCsvDropsBlanks() {
        // Act
        List<String> result = WebUtils.splitCsv("Java, , ,Git,");

        // Assert
        assertEquals(List.of("Java", "Git"), result);
    }

    @Test
    @DisplayName("splitCsv returns an empty list for null input")
    void splitCsvHandlesNull() {
        // Act
        List<String> result = WebUtils.splitCsv(null);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("splitCsv returns an empty list for a blank string")
    void splitCsvHandlesBlank() {
        // Act
        List<String> result = WebUtils.splitCsv("   ");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("joinCsv joins values with a comma and space")
    void joinCsvJoinsValues() {
        // Act
        String result = WebUtils.joinCsv(List.of("Java", "Git", "Teamwork"));

        // Assert
        assertEquals("Java, Git, Teamwork", result);
    }

    @Test
    @DisplayName("joinCsv returns an empty string for a null list")
    void joinCsvHandlesNull() {
        // Act & Assert
        assertEquals("", WebUtils.joinCsv(null));
    }

    @Test
    @DisplayName("joinCsv returns an empty string for an empty list")
    void joinCsvHandlesEmptyList() {
        // Act & Assert
        assertEquals("", WebUtils.joinCsv(List.of()));
    }

    @Test
    @DisplayName("splitCsv and joinCsv round-trip a clean comma list")
    void splitAndJoinRoundTrip() {
        // Arrange
        String original = "Java, Git, Teamwork";

        // Act
        String result = WebUtils.joinCsv(WebUtils.splitCsv(original));

        // Assert
        assertEquals(original, result);
    }

    @Test
    @DisplayName("escapeHtml escapes the angle brackets")
    void escapeHtmlEscapesAngleBrackets() {
        // Act & Assert
        assertEquals("&lt;div&gt;", WebUtils.escapeHtml("<div>"));
    }

    @Test
    @DisplayName("escapeHtml escapes the ampersand")
    void escapeHtmlEscapesAmpersand() {
        // Act & Assert
        assertEquals("Tom &amp; Jerry", WebUtils.escapeHtml("Tom & Jerry"));
    }

    @Test
    @DisplayName("escapeHtml escapes the double quote")
    void escapeHtmlEscapesQuote() {
        // Act & Assert
        assertEquals("say &quot;hi&quot;", WebUtils.escapeHtml("say \"hi\""));
    }

    @Test
    @DisplayName("escapeHtml escapes the ampersand before other entities to avoid double-escaping")
    void escapeHtmlEscapesAmpersandFirst() {
        // Act: a raw script tag with an ampersand
        String result = WebUtils.escapeHtml("<a href=\"x&y\">");

        // Assert
        assertEquals("&lt;a href=&quot;x&amp;y&quot;&gt;", result);
    }

    @Test
    @DisplayName("escapeHtml returns an empty string for null input")
    void escapeHtmlHandlesNull() {
        // Act & Assert
        assertEquals("", WebUtils.escapeHtml(null));
    }

    @Test
    @DisplayName("escapeHtml leaves plain text untouched")
    void escapeHtmlLeavesPlainTextUnchanged() {
        // Act & Assert
        assertEquals("Hello World", WebUtils.escapeHtml("Hello World"));
    }
}
