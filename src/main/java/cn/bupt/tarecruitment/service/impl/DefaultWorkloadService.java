package cn.bupt.tarecruitment.service.impl;

import cn.bupt.tarecruitment.model.ApplicantProfile;
import cn.bupt.tarecruitment.model.AssignmentRecord;
import cn.bupt.tarecruitment.model.JobPost;
import cn.bupt.tarecruitment.model.WorkloadRecommendation;
import cn.bupt.tarecruitment.model.WorkloadRow;
import cn.bupt.tarecruitment.model.WorkloadSummary;
import cn.bupt.tarecruitment.repository.JsonFileStore;
import cn.bupt.tarecruitment.service.WorkloadService;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DefaultWorkloadService implements WorkloadService {
    private static final int DEFAULT_THRESHOLD_HOURS = 12;
    private static final String PROFILES = "data/profiles.json";
    private static final String ASSIGNMENTS = "data/assignments.json";
    private static final String JOBS = "data/jobs.json";

    private final JsonFileStore store;

    public DefaultWorkloadService() {
        this(new JsonFileStore());
    }

    public DefaultWorkloadService(JsonFileStore store) {
        this.store = store;
    }

    @Override
    public WorkloadSummary buildSummary() {
        List<WorkloadRow> rows = buildRows();
        int overloadedCount = 0;
        int totalHours = 0;
        int totalAssignments = 0;
        for (WorkloadRow row : rows) {
            if (row.isOverloaded()) {
                overloadedCount++;
            }
            totalHours += row.getTotalHours();
            totalAssignments += row.getAssignmentCount();
        }
        return new WorkloadSummary(rows.size(), overloadedCount, totalAssignments, totalHours, DEFAULT_THRESHOLD_HOURS);
    }

    @Override
    public List<WorkloadRow> buildRows() {
        List<ApplicantProfile> profiles = store.readList(PROFILES, new TypeReference<List<ApplicantProfile>>() {});
        List<AssignmentRecord> assignments = store.readList(ASSIGNMENTS, new TypeReference<List<AssignmentRecord>>() {});

        Map<String, ApplicantProfile> profileById = new LinkedHashMap<>();
        for (ApplicantProfile profile : profiles) {
            profileById.put(profile.getApplicantId(), profile);
        }

        Map<String, Aggregation> aggregationByApplicant = new HashMap<>();
        for (AssignmentRecord assignment : assignments) {
            Aggregation aggregation = aggregationByApplicant.computeIfAbsent(assignment.getApplicantId(), key -> new Aggregation());
            aggregation.assignmentCount++;
            aggregation.totalHours += Math.max(0, assignment.getAssignedHours());
        }

        List<WorkloadRow> rows = new ArrayList<>();
        for (Map.Entry<String, ApplicantProfile> entry : profileById.entrySet()) {
            String applicantId = entry.getKey();
            ApplicantProfile profile = entry.getValue();
            Aggregation aggregation = aggregationByApplicant.getOrDefault(applicantId, new Aggregation());
            rows.add(new WorkloadRow(
                    applicantId,
                    profile.getName() != null && !profile.getName().isBlank() ? profile.getName() : applicantId,
                    aggregation.assignmentCount,
                    aggregation.totalHours,
                    aggregation.totalHours > DEFAULT_THRESHOLD_HOURS
            ));
        }

        rows.sort(Comparator.comparingInt(WorkloadRow::getTotalHours).reversed().thenComparing(WorkloadRow::getApplicantName));
        return rows;
    }

    @Override
    public List<WorkloadRecommendation> recommendRebalancing() {
        List<WorkloadRecommendation> recommendations = new ArrayList<>();

        List<WorkloadRow> rows = buildRows();
        List<AssignmentRecord> assignments = store.readList(ASSIGNMENTS, new TypeReference<List<AssignmentRecord>>() {});
        List<JobPost> jobs = store.readList(JOBS, new TypeReference<List<JobPost>>() {});

        Map<String, JobPost> jobById = new HashMap<>();
        for (JobPost job : jobs) {
            jobById.put(job.getJobId(), job);
        }

        // Mutable working totals so successive suggestions stay consistent.
        Map<String, Integer> hoursByApplicant = new HashMap<>();
        Map<String, String> nameByApplicant = new HashMap<>();
        for (WorkloadRow row : rows) {
            hoursByApplicant.put(row.getApplicantId(), row.getTotalHours());
            nameByApplicant.put(row.getApplicantId(), row.getApplicantName());
        }

        // Group assignment records by applicant for candidate moves.
        Map<String, List<AssignmentRecord>> assignmentsByApplicant = new HashMap<>();
        for (AssignmentRecord assignment : assignments) {
            assignmentsByApplicant
                    .computeIfAbsent(assignment.getApplicantId(), key -> new ArrayList<>())
                    .add(assignment);
        }

        // Process overloaded TAs from the most overloaded downwards (rows is sorted desc by hours).
        for (WorkloadRow row : rows) {
            String fromId = row.getApplicantId();
            int fromHours = hoursByApplicant.getOrDefault(fromId, 0);
            if (fromHours <= DEFAULT_THRESHOLD_HOURS) {
                continue;
            }

            List<AssignmentRecord> candidates = assignmentsByApplicant.getOrDefault(fromId, new ArrayList<>());
            if (candidates.isEmpty()) {
                continue;
            }

            // Pick the assignment whose removal best reduces overload: the largest
            // assignment that does not drop the TA below 0 hours. The largest one
            // gives the biggest reduction and frees the most headroom.
            AssignmentRecord bestMove = null;
            for (AssignmentRecord candidate : candidates) {
                int candidateHours = Math.max(0, candidate.getAssignedHours());
                if (candidateHours <= 0) {
                    continue;
                }
                if (bestMove == null || candidateHours > Math.max(0, bestMove.getAssignedHours())) {
                    bestMove = candidate;
                }
            }
            if (bestMove == null) {
                continue;
            }
            int moveHours = Math.max(0, bestMove.getAssignedHours());

            // Find the underloaded receiver with the lowest current total whose
            // total stays within the threshold after taking on the moved job.
            String toId = null;
            int toHours = Integer.MAX_VALUE;
            for (WorkloadRow receiver : rows) {
                String receiverId = receiver.getApplicantId();
                if (receiverId.equals(fromId)) {
                    continue;
                }
                int receiverHours = hoursByApplicant.getOrDefault(receiverId, 0);
                if (receiverHours + moveHours <= DEFAULT_THRESHOLD_HOURS && receiverHours < toHours) {
                    toId = receiverId;
                    toHours = receiverHours;
                }
            }
            if (toId == null) {
                continue;
            }

            String jobLabel = resolveJobLabel(jobById.get(bestMove.getJobId()), bestMove.getJobId());
            String fromName = nameByApplicant.getOrDefault(fromId, fromId);
            String toName = nameByApplicant.getOrDefault(toId, toId);

            String reason = fromName + " is assigned " + fromHours + "h, above the "
                    + DEFAULT_THRESHOLD_HOURS + "h limit. Moving " + jobLabel + " (" + moveHours
                    + "h) to " + toName + " (currently " + toHours + "h) keeps both within the limit.";

            recommendations.add(new WorkloadRecommendation(fromName, toName, jobLabel, moveHours, reason));

            // Apply the suggested move to the working totals.
            hoursByApplicant.put(fromId, fromHours - moveHours);
            hoursByApplicant.put(toId, toHours + moveHours);
            assignmentsByApplicant.getOrDefault(fromId, new ArrayList<>()).remove(bestMove);
        }

        return recommendations;
    }

    private String resolveJobLabel(JobPost job, String fallbackJobId) {
        if (job == null) {
            return fallbackJobId != null ? fallbackJobId : "Unknown job";
        }
        String code = job.getModuleCode() != null ? job.getModuleCode().trim() : "";
        String name = job.getModuleName() != null ? job.getModuleName().trim() : "";
        if (!code.isEmpty() && !name.isEmpty()) {
            return code + " " + name;
        }
        if (!code.isEmpty()) {
            return code;
        }
        if (!name.isEmpty()) {
            return name;
        }
        return fallbackJobId != null ? fallbackJobId : "Unknown job";
    }

    private static final class Aggregation {
        private int assignmentCount;
        private int totalHours;
    }
}
