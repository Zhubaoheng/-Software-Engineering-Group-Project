package cn.bupt.tarecruitment.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserRole {
    TA("TA"),
    MO("MO"),
    ADMIN("Admin");

    private final String code;

    UserRole(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static UserRole fromCode(String value) {
        for (UserRole role : values()) {
            if (role.code.equalsIgnoreCase(value) || role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + value);
    }
}

