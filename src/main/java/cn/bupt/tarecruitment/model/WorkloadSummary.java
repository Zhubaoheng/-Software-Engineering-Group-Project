package cn.bupt.tarecruitment.model;

public class WorkloadSummary {
    private int totalTAs;
    private int overloadedTAs;
    private int totalAssignments;
    private int totalHours;
    private int thresholdHours;

    public WorkloadSummary() {
    }

    public WorkloadSummary(int totalTAs, int overloadedTAs, int totalAssignments, int totalHours, int thresholdHours) {
        this.totalTAs = totalTAs;
        this.overloadedTAs = overloadedTAs;
        this.totalAssignments = totalAssignments;
        this.totalHours = totalHours;
        this.thresholdHours = thresholdHours;
    }

    public int getTotalTAs() {
        return totalTAs;
    }

    public void setTotalTAs(int totalTAs) {
        this.totalTAs = totalTAs;
    }

    public int getOverloadedTAs() {
        return overloadedTAs;
    }

    public void setOverloadedTAs(int overloadedTAs) {
        this.overloadedTAs = overloadedTAs;
    }

    public int getTotalAssignments() {
        return totalAssignments;
    }

    public void setTotalAssignments(int totalAssignments) {
        this.totalAssignments = totalAssignments;
    }

    public int getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }

    public int getThresholdHours() {
        return thresholdHours;
    }

    public void setThresholdHours(int thresholdHours) {
        this.thresholdHours = thresholdHours;
    }
}
