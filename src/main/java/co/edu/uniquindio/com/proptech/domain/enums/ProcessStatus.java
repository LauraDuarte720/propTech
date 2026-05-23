package co.edu.uniquindio.com.proptech.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Enum representing the status of a process.
 */
public enum ProcessStatus {
    CREATED("Created"),
    CANCELLED("Cancelled"),
    CLOSED("Closed");

    private final String displayName;

    ProcessStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    @JsonCreator
    public static ProcessStatus fromString(String value) {
        for (ProcessStatus t : values()) {
            if (t.name().equalsIgnoreCase(value) || t.displayName.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown PropertyType: " + value);
    }
}
