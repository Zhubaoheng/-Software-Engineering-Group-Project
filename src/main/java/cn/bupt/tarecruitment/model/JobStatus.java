package cn.bupt.tarecruitment.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum JobStatus {
    OPEN("Open"),
    CLOSED("Closed");

    private final String code;

    JobStatus(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static JobStatus fromCode(String value) {
        for (JobStatus status : values()) {
            if (status.code.equalsIgnoreCase(value) || status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown job status: " + value);
    }
}

