package co.edu.uniquindio.com.proptech.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Enum representing the status of a visit.
 */
public enum VisitStatus {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    COMPLETED("Completed"),
    CANCELED("Canceled"),
    RESCHEDULED("Rescheduled"),
    PENDINGRESCHEDULE("Pending Reschedule");

    private final String displayName;

    VisitStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    @JsonCreator
    public static VisitStatus fromString(String value) {
        for (VisitStatus t : values()) {
            if (t.name().equalsIgnoreCase(value) || t.displayName.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown PropertyType: " + value);
    }
}
