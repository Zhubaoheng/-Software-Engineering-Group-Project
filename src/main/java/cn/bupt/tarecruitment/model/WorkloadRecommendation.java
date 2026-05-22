package cn.bupt.tarecruitment.model;

/**
 * An explainable, advisory suggestion to move one job assignment from an
 * overloaded teaching assistant to an underloaded one. This is never an
 * automatic action; the final reassignment decision rests with the Admin.
 */
public class WorkloadRecommendation {
    private String fromApplicantName;
    private String toApplicantName;
    private String jobLabel;
    private int hours;
    private String reason;

    /** Creates an empty recommendation, primarily for JSON deserialization. */
    public WorkloadRecommendation() {
    }

    /**
     * Creates a fully populated rebalancing recommendation.
     *
     * @param fromApplicantName name of the overloaded TA giving up the job
     * @param toApplicantName   name of the underloaded TA receiving the job
     * @param jobLabel          human-readable label of the job being moved
     * @param hours             weekly hours of the moved job
     * @param reason            explanation of why the move is suggested
     */
    public WorkloadRecommendation(String fromApplicantName, String toApplicantName, String jobLabel,
                                  int hours, String reason) {
        this.fromApplicantName = fromApplicantName;
        this.toApplicantName = toApplicantName;
        this.jobLabel = jobLabel;
        this.hours = hours;
        this.reason = reason;
    }

    /** @return the name of the overloaded TA giving up the job. */
    public String getFromApplicantName() {
        return fromApplicantName;
    }

    /** @return the name of the underloaded TA receiving the job. */
    public String getToApplicantName() {
        return toApplicantName;
    }

    /** @return the human-readable label of the job being moved. */
    public String getJobLabel() {
        return jobLabel;
    }

    /** @return the weekly hours of the moved job. */
    public int getHours() {
        return hours;
    }

    /** @return the explanation of why the move is suggested. */
    public String getReason() {
        return reason;
    }
}
