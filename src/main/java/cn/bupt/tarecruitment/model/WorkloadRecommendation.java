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

    public WorkloadRecommendation() {
    }

    public WorkloadRecommendation(String fromApplicantName, String toApplicantName, String jobLabel,
                                  int hours, String reason) {
        this.fromApplicantName = fromApplicantName;
        this.toApplicantName = toApplicantName;
        this.jobLabel = jobLabel;
        this.hours = hours;
        this.reason = reason;
    }

    public String getFromApplicantName() {
        return fromApplicantName;
    }

    public String getToApplicantName() {
        return toApplicantName;
    }

    public String getJobLabel() {
        return jobLabel;
    }

    public int getHours() {
        return hours;
    }

    public String getReason() {
        return reason;
    }
}
