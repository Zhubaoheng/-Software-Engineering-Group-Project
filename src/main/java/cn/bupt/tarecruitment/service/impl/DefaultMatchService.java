package cn.bupt.tarecruitment.service.impl;

import cn.bupt.tarecruitment.model.ApplicantProfile;
import cn.bupt.tarecruitment.model.JobPost;
import cn.bupt.tarecruitment.model.SkillMatch;
import cn.bupt.tarecruitment.service.MatchService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Deterministic, rule-based implementation of {@link MatchService}.
 *
 * <p>The algorithm is intentionally simple and explainable:
 * <ol>
 *   <li>Every skill is normalised (trimmed and lower-cased).</li>
 *   <li>A required skill is "covered" when the applicant has a skill that is
 *       equal to it, or where one fully contains the other as a word-level
 *       substring (e.g. "software engineering" covers "software").</li>
 *   <li>The score is the percentage of required skills covered.</li>
 *   <li>The score maps to a STRONG / MODERATE / WEAK level.</li>
 * </ol>
 * The output is advisory only and never produces an automatic decision.
 */
public class DefaultMatchService implements MatchService {

    private static final int STRONG_THRESHOLD = 75;
    private static final int MODERATE_THRESHOLD = 40;

    public DefaultMatchService() {
    }

    @Override
    public SkillMatch match(JobPost job, ApplicantProfile profile) {
        List<String> requiredSkills = job == null || job.getRequiredSkills() == null
                ? new ArrayList<>()
                : job.getRequiredSkills();

        // Job with no required skills: trivially a full match.
        if (requiredSkills.isEmpty()) {
            return new SkillMatch(100, new ArrayList<>(), new ArrayList<>(), "STRONG",
                    "No specific skills required.");
        }

        // No profile, or a profile without any skills recorded yet.
        if (profile == null || profile.getSkills() == null || profile.getSkills().isEmpty()) {
            List<String> missing = new ArrayList<>(requiredSkills);
            return new SkillMatch(0, new ArrayList<>(), missing, "WEAK",
                    "Complete your profile skills to see your match.");
        }

        List<String> applicantSkills = profile.getSkills();

        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();
        for (String required : requiredSkills) {
            if (isCovered(required, applicantSkills)) {
                matched.add(required);
            } else {
                missing.add(required);
            }
        }

        int total = requiredSkills.size();
        int scorePercent = (int) Math.round((matched.size() * 100.0) / total);
        String level = levelFor(scorePercent);
        String explanation = buildExplanation(level, matched.size(), total, missing);

        return new SkillMatch(scorePercent, matched, missing, level, explanation);
    }

    @Override
    public Map<String, SkillMatch> matchJobsForApplicant(List<JobPost> jobs, ApplicantProfile profile) {
        Map<String, SkillMatch> result = new LinkedHashMap<>();
        if (jobs == null) {
            return result;
        }
        for (JobPost job : jobs) {
            if (job == null || job.getJobId() == null) {
                continue;
            }
            result.put(job.getJobId(), match(job, profile));
        }
        return result;
    }

    /**
     * A required skill is covered if any applicant skill is equal to it
     * (normalised) or one fully contains the other as a word-level substring.
     */
    private boolean isCovered(String required, List<String> applicantSkills) {
        String normalisedRequired = normalise(required);
        if (normalisedRequired.isEmpty()) {
            return false;
        }
        Set<String> requiredWords = words(normalisedRequired);
        for (String applicantSkill : applicantSkills) {
            String normalisedApplicant = normalise(applicantSkill);
            if (normalisedApplicant.isEmpty()) {
                continue;
            }
            if (normalisedRequired.equals(normalisedApplicant)) {
                return true;
            }
            Set<String> applicantWords = words(normalisedApplicant);
            // One side fully contains the other at word level.
            if (applicantWords.containsAll(requiredWords) || requiredWords.containsAll(applicantWords)) {
                return true;
            }
        }
        return false;
    }

    private String normalise(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }

    private Set<String> words(String normalised) {
        Set<String> words = new HashSet<>();
        for (String word : normalised.split("\\s+")) {
            if (!word.isBlank()) {
                words.add(word);
            }
        }
        return words;
    }

    private String levelFor(int scorePercent) {
        if (scorePercent >= STRONG_THRESHOLD) {
            return "STRONG";
        }
        if (scorePercent >= MODERATE_THRESHOLD) {
            return "MODERATE";
        }
        return "WEAK";
    }

    private String buildExplanation(String level, int matchedCount, int total, List<String> missing) {
        String word = switch (level) {
            case "STRONG" -> "Strong match";
            case "MODERATE" -> "Moderate match";
            default -> "Weak match";
        };
        StringBuilder sb = new StringBuilder();
        sb.append(word)
                .append(": you cover ")
                .append(matchedCount)
                .append(" of ")
                .append(total)
                .append(" required skills.");
        if (!missing.isEmpty()) {
            // Drop the closing full stop, switch to a semicolon, and continue the sentence.
            sb.setLength(sb.length() - 1);
            sb.append("; consider strengthening: ")
                    .append(String.join(", ", missing))
                    .append('.');
        }
        return sb.toString();
    }

    // Retained for clarity in tests/viva discussion; word splitting helper.
    @SuppressWarnings("unused")
    private List<String> asWordList(String normalised) {
        return Arrays.asList(normalised.split("\\s+"));
    }
}
