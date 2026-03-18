package cn.bupt.tarecruitment.service.impl;

import cn.bupt.tarecruitment.model.ApplicantProfile;
import cn.bupt.tarecruitment.model.AssignmentRecord;
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

    private static final class Aggregation {
        private int assignmentCount;
        private int totalHours;
    }
}
