package cn.bupt.tarecruitment.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Value object describing the result of the rule-based skill match between a
 * job post and an applicant profile. It is intentionally a plain POJO so it can
 * be inspected and explained at a viva.
 */
public class SkillMatch {
    private final int scorePercent;
    private final List<String> matchedSkills;
    private final List<String> missingSkills;
    private final String level;
    private final String explanation;

    /**
     * Creates an immutable skill match result.
     *
     * @param scorePercent  percentage of required skills covered (0-100)
     * @param matchedSkills required skills the applicant has; null is treated as empty
     * @param missingSkills required skills the applicant lacks; null is treated as empty
     * @param level         match level, one of STRONG, MODERATE or WEAK
     * @param explanation   human-readable explanation of the result
     */
    public SkillMatch(int scorePercent,
                      List<String> matchedSkills,
                      List<String> missingSkills,
                      String level,
                      String explanation) {
        this.scorePercent = scorePercent;
        this.matchedSkills = matchedSkills == null ? new ArrayList<>() : new ArrayList<>(matchedSkills);
        this.missingSkills = missingSkills == null ? new ArrayList<>() : new ArrayList<>(missingSkills);
        this.level = level;
        this.explanation = explanation;
    }

    /** @return the percentage of required skills covered (0-100). */
    public int getScorePercent() {
        return scorePercent;
    }

    /** @return the required skills the applicant has. */
    public List<String> getMatchedSkills() {
        return matchedSkills;
    }

    /** @return the required skills the applicant lacks. */
    public List<String> getMissingSkills() {
        return missingSkills;
    }

    /** @return the match level: STRONG, MODERATE or WEAK. */
    public String getLevel() {
        return level;
    }

    /** @return the human-readable explanation of the match result. */
    public String getExplanation() {
        return explanation;
    }

    /** @return {@code true} if at least one required skill was matched. */
    public boolean hasMatchedSkills() {
        return !matchedSkills.isEmpty();
    }

    /** @return {@code true} if at least one required skill is missing. */
    public boolean hasMissingSkills() {
        return !missingSkills.isEmpty();
    }
}
