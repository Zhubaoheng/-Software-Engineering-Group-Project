package cn.bupt.tarecruitment.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ApplicationStatus {
    SUBMITTED("Submitted"),
    UNDER_REVIEW("Under Review"),
    SHORTLISTED("Shortlisted"),
    SELECTED("Selected"),
    REJECTED("Rejected"),
    WITHDRAWN("Withdrawn");

    private final String code;

    ApplicationStatus(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static ApplicationStatus fromCode(String value) {
        for (ApplicationStatus status : values()) {
            if (status.code.equalsIgnoreCase(value) || status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown application status: " + value);
    }
}

