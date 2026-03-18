package cn.bupt.tarecruitment.model;

public class AssignmentRecord {
    private String assignmentId;
    private String jobId;
    private String applicantId;
    private int assignedHours;
    private String assignedAt;

    public AssignmentRecord() {
    }

    public AssignmentRecord(String assignmentId, String jobId, String applicantId, int assignedHours, String assignedAt) {
        this.assignmentId = assignmentId;
        this.jobId = jobId;
        this.applicantId = applicantId;
        this.assignedHours = assignedHours;
        this.assignedAt = assignedAt;
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public int getAssignedHours() {
        return assignedHours;
    }

    public void setAssignedHours(int assignedHours) {
        this.assignedHours = assignedHours;
    }

    public String getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(String assignedAt) {
        this.assignedAt = assignedAt;
    }
}
