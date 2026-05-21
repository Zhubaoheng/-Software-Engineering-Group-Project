package cn.bupt.tarecruitment.service;

import cn.bupt.tarecruitment.model.ApplicantProfile;
import cn.bupt.tarecruitment.model.JobPost;
import cn.bupt.tarecruitment.model.SkillMatch;

import java.util.List;
import java.util.Map;

/**
 * Rule-based, explainable skill-matching service. Every result is advisory
 * only: the hiring decision always rests with the Module Organiser.
 */
public interface MatchService {

    /**
     * Compute the skill match between a single job and an applicant profile.
     */
    SkillMatch match(JobPost job, ApplicantProfile profile);

    /**
     * Compute the skill match for several jobs against one applicant profile.
     *
     * @return a map of job id to its SkillMatch result.
     */
    Map<String, SkillMatch> matchJobsForApplicant(List<JobPost> jobs, ApplicantProfile profile);
}
