package cn.bupt.tarecruitment.model;

public class WorkloadRow {
    private String applicantId;
    private String applicantName;
    private int assignmentCount;
    private int totalHours;
    private boolean overloaded;

    public WorkloadRow() {
    }

    public WorkloadRow(String applicantId, String applicantName, int assignmentCount, int totalHours, boolean overloaded) {
        this.applicantId = applicantId;
        this.applicantName = applicantName;
        this.assignmentCount = assignmentCount;
        this.totalHours = totalHours;
        this.overloaded = overloaded;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public int getAssignmentCount() {
        return assignmentCount;
    }

    public void setAssignmentCount(int assignmentCount) {
        this.assignmentCount = assignmentCount;
    }

    public int getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }

    public boolean isOverloaded() {
        return overloaded;
    }

    public void setOverloaded(boolean overloaded) {
        this.overloaded = overloaded;
    }
}
