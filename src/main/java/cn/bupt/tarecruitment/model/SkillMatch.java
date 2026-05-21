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

    public int getScorePercent() {
        return scorePercent;
    }

    public List<String> getMatchedSkills() {
        return matchedSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public String getLevel() {
        return level;
    }

    public String getExplanation() {
        return explanation;
    }

    public boolean hasMatchedSkills() {
        return !matchedSkills.isEmpty();
    }

    public boolean hasMissingSkills() {
        return !missingSkills.isEmpty();
    }
}
