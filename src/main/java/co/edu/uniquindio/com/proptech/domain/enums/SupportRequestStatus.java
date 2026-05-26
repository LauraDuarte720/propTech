package co.edu.uniquindio.com.proptech.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SupportRequestStatus {
    PENDING("Pending"),
    ATTENDED("Attended"),
    CANCELLED("Cancelled");

    private final String displayName;

    SupportRequestStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    @JsonValue
    public String getKey() {
        return this.name();
    }

    @JsonCreator
    public static SupportRequestStatus fromString(String value) {
        for (SupportRequestStatus t : values()) {
            if (t.name().equalsIgnoreCase(value) || t.displayName.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown SupportRequestStatus: " + value);
    }
}