package cn.bupt.tarecruitment.service;

import cn.bupt.tarecruitment.model.WorkloadRecommendation;
import cn.bupt.tarecruitment.model.WorkloadRow;
import cn.bupt.tarecruitment.model.WorkloadSummary;

import java.util.List;

/**
 * Service that analyses teaching assistant workload across all assignments and
 * produces advisory rebalancing suggestions for the Admin.
 */
public interface WorkloadService {
    /**
     * Builds aggregate statistics over all TA workloads.
     *
     * @return a {@link WorkloadSummary} of totals, counts and the overload threshold
     */
    WorkloadSummary buildSummary();

    /**
     * Builds one workload row per TA, sorted by descending total hours.
     *
     * @return the per-applicant workload rows
     */
    List<WorkloadRow> buildRows();

    /**
     * Computes advisory suggestions for moving assignments from overloaded TAs
     * to underloaded ones so that everyone stays within the hour limit.
     *
     * @return the list of rebalancing recommendations; empty if none apply
     */
    List<WorkloadRecommendation> recommendRebalancing();
}
